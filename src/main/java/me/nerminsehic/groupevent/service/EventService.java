package me.nerminsehic.groupevent.service;

import me.nerminsehic.groupevent.entity.Attendee;
import me.nerminsehic.groupevent.entity.Event;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

public interface EventService {

    Optional<Event> findById(UUID organiserId, UUID eventId);

    List<Event> findAll(UUID organiserId);

    Event create(UUID organiserId, Event event, Set<Attendee> attendees);

    void deleteById(UUID organiserId, UUID eventId);

    void delete(UUID organiserId, Event event);

    Event updateById(UUID organiserId, UUID eventId, Event event);

    Event cancel(UUID organiserId, UUID eventId, String message);

    Event reschedule(UUID organiserId, UUID eventId, Event event);
}
