package me.nerminsehic.groupevent.service;

import com.sendgrid.Method;
import com.sendgrid.Request;
import com.sendgrid.Response;
import com.sendgrid.SendGrid;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Email;
import com.sendgrid.helpers.mail.objects.MailSettings;
import com.sendgrid.helpers.mail.objects.Personalization;
import com.sendgrid.helpers.mail.objects.TrackingSettings;
import lombok.RequiredArgsConstructor;
import me.nerminsehic.groupevent.dto.*;
import me.nerminsehic.groupevent.entity.*;
import me.nerminsehic.groupevent.exception.SendMailException;
import me.nerminsehic.groupevent.gmaps.Directions;
import me.nerminsehic.groupevent.gmaps.StaticMaps;
import me.nerminsehic.groupevent.util.Converter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SendgridMailService implements MailService {

    private final SendGrid sendgrid;
    private final MailSettings mailSettings;
    private final TrackingSettings trackingSettings;
    private final StaticMaps maps;
    private final Directions directions;

    @Value("${sendgrid.template.event-confirmation-template-id}")
    private String eventConfirmationTemplateId;

    @Value("${sendgrid.template.event-invite-template-id}")
    private String eventInviteTemplateId;

    @Value("${sendgrid.template.event-rescheduled-template-id}")
    private String eventRescheduledTemplateId;

    @Value("${sendgrid.template.invite-response-template-id}")
    private String inviteResponseTemplateId;

    @Value("${sendgrid.template.magic-link-template-id}")
    private String magicLinkTemplateId;

    @Value("${sendgrid.template.event-cancelled-template-id}")
    private String eventCancelledTemplateId;

    @Override
    public void sendLinkToOrganiser(Organiser organiser, MagicLink link) {
        Mail mail = useTemplateIdAndSender(magicLinkTemplateId, "Groupevent");
        SendMagicLinkDto magicLinkDto = new SendMagicLinkDto(
                organiser.getFirstName(),
                organiser.getLastName(),
                link.getId()
        );

        Personalization personalization = createPersonalization(
                organiser.getEmailAddress(),
                magicLinkDto
        );
        mail.addPersonalization(personalization);

        send(mail);
    }

    @Override
    public void sendEventConfirmationToOrganiser(Event event) {
        Mail mail = useTemplateIdAndSender(eventConfirmationTemplateId, "Groupevent");
        SendEventConfirmationEmailDto sendConfirmationDto = new SendEventConfirmationEmailDto(event);
        Organiser organiser = event.getOrganiser();

        Personalization personalization = createPersonalization(
                organiser.getEmailAddress(),
                sendConfirmationDto
        );
        mail.addPersonalization(personalization);

        send(mail);
    }

    @Override
    public void sendInvitesToAttendees(Event event) {
        String sender = "%s %s (via Groupevent)".formatted(event.getOrganiser().getFirstName(), event.getOrganiser().getLastName());
        Mail mail = useTemplateIdAndSender(eventInviteTemplateId, sender);

        Address address = event.getAddress();
        String mapLink = maps.create(address);
        String directionsLink = directions.getDirectionsLink(address);

        Set<Personalization> personalizationSet = createPersonalizationForEachInvite(
                event.getInvites(),
                attendee -> new SendInvitesEmailDto(event, attendee, mapLink, directionsLink)
        );
        personalizationSet.forEach(mail::addPersonalization);

        send(mail);
    }

    @Override
    public void sendAttendeeResponseToOrganiser(Invite invite) {
        Event event = invite.getEvent();
        Attendee attendee = invite.getAttendee();
        Organiser organiser = event.getOrganiser();

        String sender = "%s %s (via Groupevent)".formatted(attendee.getFirstName(), attendee.getLastName());
        Mail mail = useTemplateIdAndSender(inviteResponseTemplateId, sender);

        Personalization personalization = createPersonalization(
                organiser.getEmailAddress(),
                new InviteResponseEmailDto(invite)
        );
        mail.addPersonalization(personalization);

        send(mail);
    }

    @Override
    public void sendRescheduledNoticeToAttendees(Event event) {
        Organiser organiser = event.getOrganiser();
        Address address = event.getAddress();
        String mapLink = maps.create(address);
        String directionsLink = directions.getDirectionsLink(address);

        String sender = "%s %s (via Groupevent)".formatted(organiser.getFirstName(), organiser.getLastName());
        Mail mail = useTemplateIdAndSender(eventRescheduledTemplateId, sender);

        Set<Personalization> personalizationSet = createPersonalizationForEachInvite(
                event.getInvites(),
                attendee -> new SendInvitesEmailDto(event, attendee, mapLink, directionsLink)
        );
        personalizationSet.forEach(mail::addPersonalization);

        send(mail);
    }

    @Override
    public void sendCancellationNoticeToAttendees(Event event) {
        Organiser organiser = event.getOrganiser();
        String sender = "%s %s (via Groupevent)".formatted(organiser.getFirstName(), organiser.getLastName());

        Mail mail = useTemplateIdAndSender(eventCancelledTemplateId, sender);
        Set<Personalization> personalizationSet = createPersonalizationForEachInvite(
                event.getInvites(),
                attendee -> new SendCancellationEmailDto(event, attendee)
        );

        personalizationSet.forEach(mail::addPersonalization);
        send(mail);
    }

    private boolean send(Mail mail) {
        Request req = initRequest(mail);
        Response res;

        try {
            res = sendgrid.api(req);
        } catch (IOException ex) {
            throw new SendMailException(ex);
        }

        return res.getStatusCode() >= 200 && res.getStatusCode() < 300;
    }

    private Request initRequest(Mail mail) {
        Request request = new Request();
        request.setMethod(Method.POST);
        request.setEndpoint("/mail/send");

        try {
            request.setBody(mail.build());
        } catch (IOException ex) {
            throw new SendMailException(ex); // exception translation
        }

        return request;
    }

    private Mail useTemplateIdAndSender(String templateId, String sender) {
        Mail mail = new Mail();
        mail.setFrom(new Email("invites@groupevent.co", sender));
        mail.setReplyTo(new Email("invites@groupevent.co", sender));
        mail.setTemplateId(templateId);
        mail.setMailSettings(mailSettings);
        mail.setTrackingSettings(trackingSettings);

        return mail;
    }

    private Set<Personalization> createPersonalizationForEachInvite(Set<Invite> invites, Function<Attendee, Object> personalizeFn) {
        return invites.stream()
                .map(Invite::getAttendee)
                .map(attendee -> createPersonalization(
                        attendee.getEmailAddress(),
                        personalizeFn.apply(attendee)
                ))
                .collect(Collectors.toSet());
    }

    private Personalization createPersonalization(String email, Object dto) {
        Personalization personalization = new Personalization();
        Map<String, Object> data = Converter.objectToMap(dto);

        personalization.addTo(new Email(email));
        data.keySet().forEach(key -> personalization.addDynamicTemplateData(key, data.get(key)));

        return personalization;
    }
}
