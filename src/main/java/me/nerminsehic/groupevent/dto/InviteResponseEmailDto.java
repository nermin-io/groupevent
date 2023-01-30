package me.nerminsehic.groupevent.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import me.nerminsehic.groupevent.entity.*;

import java.util.UUID;

@Data
@ToString
@NoArgsConstructor
public class InviteResponseEmailDto {

    @JsonProperty("attendee")
    private AttendeeDto attendee;

    @JsonProperty("response")
    private InviteResponse response;

    @JsonProperty("message")
    private String messageHtml;

    @JsonProperty("event_id")
    private UUID eventId;

    @JsonProperty("event_name")
    private String eventName;

    @JsonProperty("event_description")
    private String eventDescriptionHtml;

    @JsonProperty("organiser")
    private OrganiserDto organiser;

    public InviteResponseEmailDto(Invite invite) {
        Attendee attendee = invite.getAttendee();
        Event event = invite.getEvent();
        Organiser organiser = event.getOrganiser();

        this.response = invite.getResponse();
        this.messageHtml = invite.getMessage() != null ? invite.getMessage().replaceAll("(\r\n|\n)", "<br>") : null;

        this.attendee = new AttendeeDto(
                attendee.getId(),
                attendee.getFirstName(),
                attendee.getLastName(),
                attendee.getEmailAddress()
        );
        this.eventId = event.getId();
        this.eventName = event.getName();
        this.eventDescriptionHtml = event.getDescription().replaceAll("(\r\n|\n)", "<br>");
        this.organiser = new OrganiserDto(organiser);
    }
}
