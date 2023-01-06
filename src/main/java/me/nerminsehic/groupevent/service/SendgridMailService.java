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
    public boolean sendMagicLink(Organiser organiser, MagicLink link) {
        SendMagicLinkDto data = new SendMagicLinkDto(
                organiser.getFirstName(),
                organiser.getLastName(),
                link.getId()
        );

        Mail mail = useTemplateId(magicLinkTemplateId, "Groupevent");
        mail.addPersonalization(createPersonalization(organiser.getEmailAddress(), data));
        mail.setSubject("Verify your login");

        return send(mail);
    }

    @Override
    public boolean sendEventConfirmation(Event event) {
        SendEventConfirmationEmailDto data = new SendEventConfirmationEmailDto(event);
        Organiser organiser = event.getOrganiser();

        Mail mail = useTemplateId(eventConfirmationTemplateId, "Groupevent");
        mail.addPersonalization(createPersonalization(organiser.getEmailAddress(), data));
        mail.setSubject("Let's get this party started! Prepare for %s.".formatted(event.getName()));

        return send(mail);
    }

    @Override
    public boolean sendInvites(Event event) {
        String sender = "%s %s (via Groupevent)".formatted(event.getOrganiser().getFirstName(), event.getOrganiser().getLastName());
        Mail mail = applyPersonalization(
                useTemplateId(eventInviteTemplateId, sender),
                createInvitePersonalizationSet(event)
        );
        mail.setSubject("You've been invited to %s!".formatted(event.getName()));
        return send(mail);
    }

    @Override
    public boolean sendInviteResponse(Invite invite) {
        Event event = invite.getEvent();
        Attendee attendee = invite.getAttendee();

        String sender = "%s %s (via Groupevent)".formatted(attendee.getFirstName(), attendee.getLastName());
        Mail mail = useTemplateId(inviteResponseTemplateId, sender);
        mail.addPersonalization(createInviteResponsePersonalization(invite));

        String subject = "Re: %s - I'm %s".formatted(
                event.getName(),
                invite.getResponse() == InviteResponse.GOING ? "going!" : "not going."
        );
        mail.setSubject(subject);

        return send(mail);
    }


    @Override
    public boolean sendRescheduledNotice(Event event) {
        Organiser organiser = event.getOrganiser();
        String sender = "%s %s (via Groupevent)".formatted(organiser.getFirstName(), organiser.getLastName());
        Mail mail = applyPersonalization(
                useTemplateId(eventRescheduledTemplateId, sender),
                createInvitePersonalizationSet(event)
        );
        mail.setSubject("%s has been rescheduled".formatted(event.getName()));
        return send(mail);
    }

    @Override
    public boolean sendCancellationNotice(Event event) {
        Organiser organiser = event.getOrganiser();
        String sender = "%s %s (via Groupevent)".formatted(organiser.getFirstName(), organiser.getLastName());
        Mail mail = applyPersonalization(
                useTemplateId(eventCancelledTemplateId, sender),
                createCancellationPersonalizationSet(event)
        );
        mail.setSubject("%s has been cancelled.".formatted(event.getName()));
        return send(mail);
    }

    private boolean send(Mail mail) {
        Request req = initialiseMailRequest(mail);
        Response res;

        try {
            res = sendgrid.api(req);
        } catch(IOException ex) {
            throw new SendMailException(ex);
        }

        return res.getStatusCode() >= 200 && res.getStatusCode() < 300;
    }

    private Request initialiseMailRequest(Mail mail) {
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

    private Mail applyPersonalization(Mail template, Set<Personalization> personalizationSet) {
        personalizationSet.forEach(template::addPersonalization);
        return template;
    }

    private Mail useTemplateId(String templateId, String sender) {
        Mail mail = new Mail();
        mail.setFrom(new Email("invites@groupevent.co", sender));
        mail.setReplyTo(new Email("invites@groupevent.co", sender));
        mail.setTemplateId(templateId);
        mail.setMailSettings(mailSettings);
        mail.setTrackingSettings(trackingSettings);

        return mail;
    }

    private Set<Personalization> createInvitePersonalizationSet(Event event) {
        Address address = event.getAddress();

        String mapLink = maps.create(address);
        String directionsLink = directions.getDirectionsLink(address);

        return event.getInvites()
                .stream()
                .map(Invite::getAttendee)
                .map(attendee -> {
                    Map<String, Object> data = Converter.objectToMap(new SendInvitesEmailDto(event, attendee, mapLink, directionsLink));
                    Personalization p = new Personalization();
                    p.addTo(new Email(attendee.getEmailAddress()));
                    data.keySet().forEach(key -> p.addDynamicTemplateData(key, data.get(key)));

                    return p;
                })
                .collect(Collectors.toSet());
    }

    private Set<Personalization> createCancellationPersonalizationSet(Event event) {
        return event.getInvites()
                .stream()
                .map(Invite::getAttendee)
                .map(attendee -> {
                    Map<String, Object> data = Converter.objectToMap(new SendCancellationEmailDto(event, attendee));
                    Personalization p = new Personalization();
                    p.addTo(new Email(attendee.getEmailAddress()));
                    data.keySet().forEach(key -> p.addDynamicTemplateData(key, data.get(key)));

                    return p;
                })
                .collect(Collectors.toSet());
    }

    private Personalization createPersonalization(String email, Object obj) {
        Personalization personalization = new Personalization();
        Map<String, Object> data = Converter.objectToMap(obj);

        personalization.addTo(new Email(email));
        data.keySet().forEach(key -> personalization.addDynamicTemplateData(key, data.get(key)));

        return personalization;
    }

    private Personalization createInviteResponsePersonalization(Invite invite) {
        Personalization personalization = new Personalization();
        Map<String, Object> data = Converter.objectToMap(new InviteResponseEmailDto(invite));
        Event event = invite.getEvent();

        personalization.addTo(new Email(event.getOrganiser().getEmailAddress()));
        data.keySet().forEach(key -> personalization.addDynamicTemplateData(key, data.get(key)));

        return personalization;
    }

}
