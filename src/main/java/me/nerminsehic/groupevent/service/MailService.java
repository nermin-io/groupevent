package me.nerminsehic.groupevent.service;

import me.nerminsehic.groupevent.entity.*;

public interface MailService {

    boolean sendMagicLink(Organiser organiser, MagicLink link);

    boolean sendInvites(Event event);

    boolean sendInviteResponse(Invite invite);

    boolean sendCancellationNotice(Event event);

    boolean sendRescheduledNotice(Event event);

    boolean sendEventConfirmation(Event event);

}
