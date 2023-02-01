package me.nerminsehic.groupevent.controller;

import lombok.RequiredArgsConstructor;
import me.nerminsehic.groupevent.dto.EventCancelDto;
import me.nerminsehic.groupevent.dto.EventDto;
import me.nerminsehic.groupevent.entity.Attendee;
import me.nerminsehic.groupevent.entity.Event;
import me.nerminsehic.groupevent.exception.IllegalAccessTokenException;
import me.nerminsehic.groupevent.exception.NotFoundException;
import me.nerminsehic.groupevent.service.AttendeeService;
import me.nerminsehic.groupevent.service.EventAccessTokenService;
import me.nerminsehic.groupevent.service.EventService;
import me.nerminsehic.groupevent.util.Encoder;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;
import java.util.UUID;

@RestController
@RequestMapping("api/v1/organisers/{organiserId}/events")
@RequiredArgsConstructor
public class EventController {

    private final EventService eventService;
    private final AttendeeService attendeeService;
    private final EventAccessTokenService eventAccessTokenService;
    private final ModelMapper modelMapper;

    private Event convertToEntity(EventDto eventDto) {
        return modelMapper.map(eventDto, Event.class);
    }

    private EventDto convertToDto(Event event) {
        return modelMapper.map(event, EventDto.class);
    }

    @CrossOrigin(origins = {"http://localhost:3000", "https://groupevent.co"})
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public EventDto createEvent(@PathVariable UUID organiserId, @Validated(EventDto.FullValidation.class) @RequestBody EventDto eventDto) {
        Event event = convertToEntity(eventDto);
        Set<Attendee> attendees = attendeeService.findOrCreateAttendeesByEmail(eventDto.getAttendees());

        return convertToDto(eventService.create(organiserId, event, attendees));
    }

    @CrossOrigin(origins = {"http://localhost:3000", "https://groupevent.co"})
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<EventDto> getEvents(@PathVariable UUID organiserId) {
        return eventService.findAll(organiserId)
                .stream()
                .map(this::convertToDto)
                .toList();
    }

    @CrossOrigin(origins = {"http://localhost:3000", "https://groupevent.co"})
    @GetMapping("{eventId}")
    @ResponseStatus(HttpStatus.OK)
    public EventDto getEventById(@PathVariable UUID organiserId, @PathVariable UUID eventId) {
        Event event = eventService.findById(organiserId, eventId)
                .orElseThrow(() -> new NotFoundException(Event.class, eventId));

        return convertToDto(event);
    }

    @CrossOrigin(origins = {"http://localhost:3000", "https://groupevent.co"})
    @DeleteMapping("{eventId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public String deleteEventById(@PathVariable UUID organiserId, @PathVariable UUID eventId) {
        eventService.deleteById(organiserId, eventId);
        return "Successfully deleted event %s".formatted(eventId);
    }

    @CrossOrigin(origins = {"http://localhost:3000", "https://groupevent.co"})
    @PutMapping("{eventId}")
    @ResponseStatus(HttpStatus.OK)
    public EventDto updateEventById(@PathVariable UUID organiserId, @PathVariable UUID eventId, @Validated(EventDto.FullValidation.class) @RequestBody EventDto eventDto) {
        Event event = convertToEntity(eventDto);

        return convertToDto(eventService.updateById(organiserId, eventId, event));
    }

    @CrossOrigin(origins = {"http://localhost:3000", "https://groupevent.co"})
    @PatchMapping("{eventId}/cancel")
    @ResponseStatus(HttpStatus.OK)
    public EventDto cancelEvent(@RequestParam String token, @PathVariable UUID organiserId, @PathVariable UUID eventId, @Validated @RequestBody EventCancelDto eventCancelDto) {
        if(!isValidToken(token, eventId, organiserId))
            throw new IllegalAccessTokenException("Invalid token");

        Event cancelledEvent = eventService.cancel(organiserId, eventId, eventCancelDto.getMessage());

        return convertToDto(cancelledEvent);
    }

    @CrossOrigin(origins = {"http://localhost:3000", "https://groupevent.co"})
    @PatchMapping("{eventId}/reschedule")
    @ResponseStatus(HttpStatus.OK)
    public EventDto rescheduleEvent(@RequestParam String token, @PathVariable UUID organiserId, @PathVariable UUID eventId, @Validated @RequestBody EventDto eventDto) {
        if(!isValidToken(token, eventId, organiserId))
            throw new IllegalAccessTokenException("Invalid token");

        Event event = convertToEntity(eventDto);
        return convertToDto(eventService.reschedule(organiserId, eventId, event));
    }

    private boolean isValidToken(String token, UUID eventId, UUID organiserId) {
        Event verifiedEvent = eventAccessTokenService.verifyToken(token);

        return verifiedEvent != null && verifiedEvent.getId().equals(eventId) && verifiedEvent.getOrganiser().getId().equals(organiserId);
    }

}
