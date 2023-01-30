package me.nerminsehic.groupevent.controller;

import lombok.RequiredArgsConstructor;
import me.nerminsehic.groupevent.dto.EventAccessTokenDto;
import me.nerminsehic.groupevent.dto.EventDto;
import me.nerminsehic.groupevent.entity.Event;
import me.nerminsehic.groupevent.service.EventAccessTokenService;
import org.modelmapper.ModelMapper;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/event_tokens")
@RequiredArgsConstructor
public class EventAccessController {

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
    public EventDto validateToken(@RequestBody EventAccessTokenDto eventAccessTokenDto) {
        Event event = eventAccessTokenService.verifyToken(eventAccessTokenDto.getToken());
        return convertToDto(event);
    }

}

