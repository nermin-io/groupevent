package me.nerminsehic.groupevent.service;

import me.nerminsehic.groupevent.entity.*;

public interface MailService {

    void sendLinkToOrganiser(Organiser organiser, MagicLink link);

    void sendInvitesToAttendees(Event event);

    void sendAttendeeResponseToOrganiser(Invite invite);

    void sendCancellationNoticeToAttendees(Event event);

    void sendRescheduledNoticeToAttendees(Event event);

    void sendEventConfirmationToOrganiser(Event event);

}
