package me.nerminsehic.groupevent.controller;

import me.nerminsehic.groupevent.dto.EventCancelDto;
import me.nerminsehic.groupevent.dto.EventDto;
import me.nerminsehic.groupevent.entity.Attendee;
import me.nerminsehic.groupevent.entity.Event;
import me.nerminsehic.groupevent.exception.NotFoundException;
import me.nerminsehic.groupevent.service.AttendeeService;
import me.nerminsehic.groupevent.service.EventService;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;
import java.util.UUID;

@RestController
@RequestMapping("api/v1/organisers/{organiserId}/events")
public class EventController {

    private final EventService eventService;
    private final AttendeeService attendeeService;
    private final ModelMapper modelMapper;

    private Event convertToEntity(EventDto eventDto) {
        return modelMapper.map(eventDto, Event.class);
    }

    private EventDto convertToDto(Event event) {
        return modelMapper.map(event, EventDto.class);
    }

    public EventController(EventService eventService, AttendeeService attendeeService, ModelMapper modelMapper) {
        this.eventService = eventService;
        this.attendeeService = attendeeService;
        this.modelMapper = modelMapper;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public EventDto createEvent(@PathVariable UUID organiserId, @RequestBody EventDto eventDto) {
        Event event = convertToEntity(eventDto);
        Set<Attendee> attendees = attendeeService.findOrCreateAttendeesByEmail(eventDto.getAttendees());

        return convertToDto(eventService.create(organiserId, event, attendees));
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<EventDto> getEvents(@PathVariable UUID organiserId) {
        return eventService.findAll(organiserId)
                .stream()
                .map(this::convertToDto)
                .toList();
    }

    @GetMapping("{eventId}")
    @ResponseStatus(HttpStatus.OK)
    public EventDto getEventById(@PathVariable UUID organiserId, @PathVariable UUID eventId) {
        Event event = eventService.findById(organiserId, eventId)
                .orElseThrow(() -> new NotFoundException(Event.class, eventId));

        return convertToDto(event);
    }

    @DeleteMapping("{eventId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public String deleteEventById(@PathVariable UUID organiserId, @PathVariable UUID eventId) {
        eventService.deleteById(organiserId, eventId);
        return "Successfully deleted event %s".formatted(eventId);
    }

    @PutMapping("{eventId}")
    @ResponseStatus(HttpStatus.OK)
    public EventDto updateEventById(@PathVariable UUID organiserId, @PathVariable UUID eventId, @RequestBody EventDto eventDto) {
        Event event = convertToEntity(eventDto);

        return convertToDto(eventService.updateById(organiserId, eventId, event));
    }

    @PatchMapping("{eventId}/cancel")
    @ResponseStatus(HttpStatus.OK)
    public EventDto cancelEvent(@PathVariable UUID organiserId, @PathVariable UUID eventId, @RequestBody EventCancelDto eventCancelDto) {
        Event cancelledEvent = eventService.cancel(organiserId, eventId, eventCancelDto.getMessage());

        return convertToDto(cancelledEvent);
    }

    @PatchMapping("{eventId}/reschedule")
    @ResponseStatus(HttpStatus.OK)
    public EventDto rescheduleEvent(@PathVariable UUID organiserId, @PathVariable UUID eventId, @RequestBody EventDto eventDto) {
        Event event = convertToEntity(eventDto);
        return convertToDto(eventService.reschedule(organiserId, eventId, event));
    }

}
