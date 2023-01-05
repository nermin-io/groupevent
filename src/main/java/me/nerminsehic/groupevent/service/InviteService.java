package me.nerminsehic.groupevent.service;

import me.nerminsehic.groupevent.entity.Invite;
import me.nerminsehic.groupevent.entity.InviteResponse;

import java.util.UUID;

public interface InviteService {

    Invite respondInvite(UUID eventId, UUID attendeeId, String firstName, String lastName, InviteResponse response, String message);

    Invite getInvite(UUID eventId, UUID attendeeId);
}
