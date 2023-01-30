package me.nerminsehic.groupevent.service;

import lombok.RequiredArgsConstructor;
import me.nerminsehic.groupevent.entity.*;
import me.nerminsehic.groupevent.exception.IllegalOperationException;
import me.nerminsehic.groupevent.exception.NotFoundException;
import me.nerminsehic.groupevent.repository.Events;
import org.springframework.stereotype.Service;

import java.time.Clock;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {

    private final Events events;
    private final OrganiserService organiserService;
    private final AddressService addressService;
    private final AttendeeService attendeeService;
    private final MailService mailService;
    private final EventAccessTokenService eventAccessTokenService;
    private final Clock clock;

    @Override
    public Optional<Event> findById(UUID organiserId, UUID eventId) {
        Organiser organiser = organiserService.getOrganiserById(organiserId);
        return events.findByIdAndOrganiser(eventId, organiser);
    }

    @Override
    public List<Event> findAll(UUID organiserId) {
        Organiser organiser = organiserService.getOrganiserById(organiserId);
        return events.findAllByOrganiser(organiser);
    }

    @Override
    public Event create(UUID organiserId, Event event, Set<Attendee> attendees) {
        Organiser organiser = organiserService.getOrganiserById(organiserId);
        Address address = addressService.findOrCreateAddress(organiserId, event.getAddress());

        Event newEvent = new Event(
                organiser,
                address,
                attendees,
                event.getName(),
                event.getDescription(),
                event.getScheduledDate(),
                event.getTimeFrom(),
                event.getTimeTo(),
                event.getAgenda()
        );
        newEvent.setUpdatedAt(Instant.now(clock));
        newEvent.setCreatedAt(Instant.now(clock));
        newEvent.setStatus(EventStatus.PLANNED);

        Event persistedEvent = events.save(newEvent);
        mailService.sendInvitesToAttendees(persistedEvent);

        String accessToken = eventAccessTokenService.createToken(persistedEvent);
        mailService.sendEventConfirmationToOrganiser(persistedEvent, accessToken);

        attendeeService.updateLastInvited(attendees);
        return persistedEvent;
    }

    @Override
    public Event updateById(UUID organiserId, UUID eventId, Event newEvent) {
        Organiser organiser = organiserService.getOrganiserById(organiserId);
        Address address = addressService.findOrCreateAddress(organiserId, newEvent.getAddress());

        Event currentEvent = events.findByIdAndOrganiser(eventId, organiser)
                .orElseThrow(() -> new NotFoundException(Event.class, eventId));

        return updateEvent(currentEvent, newEvent, address);
    }

    @Override
    public Event cancel(UUID organiserId, UUID eventId, String message) {
        Organiser organiser = organiserService.getOrganiserById(organiserId);
        Event event = events.findByIdAndOrganiser(eventId, organiser)
                .orElseThrow(() -> new NotFoundException(Event.class, eventId));

        if (event.getStatus() == EventStatus.CANCELLED)
            throw new IllegalOperationException("Cannot cancel event that is already cancelled.");

        event.setStatus(EventStatus.CANCELLED);
        event.setUpdatedAt(Instant.now(clock));
        event.setCancelMessage(message);

        Event persistedEvent = events.save(event);
        mailService.sendCancellationNoticeToAttendees(persistedEvent);

        return persistedEvent;
    }

    @Override
    public Event reschedule(UUID organiserId, UUID eventId, Event newEvent) {
        Organiser organiser = organiserService.getOrganiserById(organiserId);
        Event event = events.findByIdAndOrganiser(eventId, organiser)
                .orElseThrow(() -> new NotFoundException(Event.class, eventId));

        if (event.getStatus() == EventStatus.CANCELLED)
            throw new IllegalOperationException("Cannot reschedule a cancelled event.");

        event.setScheduledDate(newEvent.getScheduledDate());
        event.setTimeFrom(newEvent.getTimeFrom());
        event.setTimeTo(newEvent.getTimeTo());
        event.setAgenda(newEvent.getAgenda());
        event.setUpdatedAt(Instant.now(clock));
        event.setStatus(EventStatus.RESCHEDULED);

        Event persistedEvent = events.save(event);
        mailService.sendRescheduledNoticeToAttendees(persistedEvent);

        return persistedEvent;
    }

    @Override
    public void deleteById(UUID organiserId, UUID eventId) {
        Organiser organiser = organiserService.getOrganiserById(organiserId);
        Event event = events.findByIdAndOrganiser(eventId, organiser)
                .orElseThrow(() -> new NotFoundException(Event.class, eventId));

        events.delete(event);
    }

    @Override
    public void delete(UUID organiserId, Event event) {
        Organiser organiser = organiserService.getOrganiserById(organiserId);
        if (!event.getOrganiser().equals(organiser))
            throw new IllegalOperationException("Attempt to illegally delete an event failed.");

        events.delete(event);
    }

    private Event updateEvent(Event currentEvent, Event newEvent, Address address) {
        currentEvent.setName(newEvent.getName());
        currentEvent.setDescription(newEvent.getDescription());
        currentEvent.setAddress(address);
        currentEvent.setAgenda(newEvent.getAgenda());
        currentEvent.setScheduledDate(newEvent.getScheduledDate());
        currentEvent.setTimeFrom(newEvent.getTimeFrom());
        currentEvent.setTimeTo(newEvent.getTimeTo());
        currentEvent.setUpdatedAt(Instant.now(clock));

        return events.save(currentEvent);
    }
}
