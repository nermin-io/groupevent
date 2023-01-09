package me.nerminsehic.groupevent.service;

import lombok.RequiredArgsConstructor;
import me.nerminsehic.groupevent.entity.*;
import me.nerminsehic.groupevent.entity.key.InviteCompositeKey;
import me.nerminsehic.groupevent.exception.IllegalOperationException;
import me.nerminsehic.groupevent.exception.NotFoundException;
import me.nerminsehic.groupevent.repository.Invites;
import org.springframework.stereotype.Service;

import java.time.Clock;
import java.time.Instant;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class InviteServiceImpl implements InviteService {

    private final Invites invites;
    private final AttendeeService attendeeService;
    private final MailService mailService;
    private final Clock clock;

    @Override
    public Invite respondInvite(UUID eventId, UUID attendeeId, String firstName, String lastName, InviteResponse response, String message) {
        Invite invite = getInvite(eventId, attendeeId);
        Event event = invite.getEvent();

        if(event.getStatus() == EventStatus.CANCELLED)
            throw new IllegalOperationException("This event has been cancelled");

        attendeeService.updateName(attendeeId, firstName, lastName);
        invite.setResponse(response);
        invite.setMessage(message);
        invite.setUpdatedAt(Instant.now(clock));

        Invite persistedInvite = invites.save(invite);

        mailService.sendAttendeeResponseToOrganiser(persistedInvite);
        return persistedInvite;
    }

    @Override
    public Invite getInvite(UUID eventId, UUID attendeeId) {
        InviteCompositeKey id = new InviteCompositeKey(eventId, attendeeId);
        return invites.findById(id)
                .orElseThrow(() -> new NotFoundException(Invite.class, id));
    }
}
