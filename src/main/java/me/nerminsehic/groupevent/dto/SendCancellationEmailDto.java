package me.nerminsehic.groupevent.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import me.nerminsehic.groupevent.entity.Attendee;
import me.nerminsehic.groupevent.entity.Event;

import java.util.UUID;

@Data
@NoArgsConstructor
@ToString
public class SendCancellationEmailDto {

    @JsonProperty("attendee")
    private AttendeeDto attendee;

    @JsonProperty("cancel_message")
    private String cancelMessage;

    @JsonProperty("event_id")
    private UUID eventId;

    @JsonProperty("event_name")
    private String eventName;

    @JsonProperty("event_description")
    private String eventDescription;

    @JsonProperty("organiser")
    private OrganiserDto organiser;

    public SendCancellationEmailDto(Event event, Attendee attendee) {
        this.attendee = new AttendeeDto(
                attendee.getId(),
                attendee.getFirstName(),
                attendee.getLastName(),
                attendee.getEmailAddress()
        );
        this.organiser = new OrganiserDto(event.getOrganiser());
        this.cancelMessage = event.getCancelMessage();
        this.eventId = event.getId();
        this.eventName = event.getName();
        this.eventDescription = event.getDescription();
    }
}