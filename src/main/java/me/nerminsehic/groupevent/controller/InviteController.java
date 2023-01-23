package me.nerminsehic.groupevent.controller;

import lombok.RequiredArgsConstructor;
import me.nerminsehic.groupevent.dto.InviteDto;
import me.nerminsehic.groupevent.dto.InviteResponseDto;
import me.nerminsehic.groupevent.entity.Invite;
import me.nerminsehic.groupevent.service.InviteService;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("api/v1/attendees/{attendeeId}/invites")
@RequiredArgsConstructor
public class InviteController {

    private final InviteService inviteService;
    private final ModelMapper modelMapper;

    private Invite convertToEntity(InviteDto inviteDto) {
        return modelMapper.map(inviteDto, Invite.class);
    }

    private InviteDto convertToDto(Invite invite) {
        return modelMapper.map(invite, InviteDto.class);
    }

    @CrossOrigin(origins = {"http://localhost:3000", "https://groupevent.co"})
    @PatchMapping("{eventId}")
    @ResponseStatus(HttpStatus.OK)
    public String respondInvite(@PathVariable UUID attendeeId, @PathVariable UUID eventId, @Validated @RequestBody InviteResponseDto responseDto) {

        inviteService.respondInvite(
                eventId,
                attendeeId,
                responseDto.getFirstName(),
                responseDto.getLastName(),
                responseDto.getResponse(),
                responseDto.getMessage()
        );

        return "Successfully responded to event %s".formatted(eventId);
    }

    @CrossOrigin(origins = {"http://localhost:3000", "https://groupevent.co"})
    @GetMapping("{eventId}")
    @ResponseStatus(HttpStatus.OK)
    public InviteDto getInvite(@PathVariable UUID attendeeId, @PathVariable UUID eventId) {
        return convertToDto(inviteService.getInvite(eventId, attendeeId));
    }
}
