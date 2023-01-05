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
    private String message;

    @JsonProperty("event_id")
    private UUID eventId;

    @JsonProperty("event_name")
    private String eventName;

    @JsonProperty("event_description")
    private String eventDescription;

    @JsonProperty("organiser")
    private OrganiserDto organiser;

    public InviteResponseEmailDto(Invite invite) {
        Attendee attendee = invite.getAttendee();
        Event event = invite.getEvent();
        Organiser organiser = event.getOrganiser();

        this.response = invite.getResponse();
        this.message = invite.getMessage();

        this.attendee = new AttendeeDto(
                attendee.getId(),
                attendee.getFirstName(),
                attendee.getLastName(),
                attendee.getEmailAddress()
        );
        this.eventId = event.getId();
        this.eventName = event.getName();
        this.eventDescription = event.getDescription();
        this.organiser = new OrganiserDto(organiser);
    }
}
