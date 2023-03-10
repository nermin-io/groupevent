package me.nerminsehic.groupevent.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.ToString;
import me.nerminsehic.groupevent.entity.Attendee;
import me.nerminsehic.groupevent.entity.Event;
import me.nerminsehic.groupevent.entity.Address;
import me.nerminsehic.groupevent.entity.Organiser;

import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

@Data
@ToString
public class SendInvitesEmailDto {

    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("EEEE, MMM dd, yyyy");
    private static final DateTimeFormatter TIME_FORMAT = DateTimeFormatter.ofPattern("hh:mm a");

    @JsonProperty("attendee_id")
    private UUID attendeeId;

    @JsonProperty("email_address")
    private String emailAddress;

    @JsonProperty("event_id")
    private UUID eventId;

    @JsonProperty("address")
    private AddressDto address;

    @JsonProperty("name")
    private String name;

    @JsonProperty("description")
    private String descriptionHtml;

    @JsonProperty("scheduled_date")
    private String scheduledDate;

    @JsonProperty("time_from")
    private String timeFrom;

    @JsonProperty("time_to")
    private String timeTo;

    @JsonProperty("agenda")
    private String agendaHtml;

    @JsonProperty("duration_in_hours")
    private long durationInHours;

    @JsonProperty("organiser")
    private OrganiserDto organiser;

    @JsonProperty("map_link")
    private String mapLink;

    @JsonProperty("directions_link")
    private String directionsLink;

    public SendInvitesEmailDto(Event event, Attendee attendee, String mapLink, String directionsLink) {
        this.attendeeId = attendee.getId();
        this.emailAddress = attendee.getEmailAddress();
        this.eventId = event.getId();
        this.name = event.getName();
        this.descriptionHtml = event.getDescription().replaceAll("(\r\n|\n)", "<br>");
        this.agendaHtml = event.getAgenda().replaceAll("(\r\n|\n)", "<br>");
        this.scheduledDate = event.getScheduledDate().format(DATE_FORMAT);
        this.timeFrom = event.getTimeFrom().format(TIME_FORMAT);
        this.timeTo = event.getTimeTo().format(TIME_FORMAT);
        this.durationInHours = event.getTimeFrom().until(event.getTimeTo(), ChronoUnit.HOURS);
        this.mapLink = mapLink;
        this.directionsLink = directionsLink;

        Address address = event.getAddress();
        this.address = new AddressDto(
                address.getAddress(),
                address.getAddress2(),
                address.getCity(),
                address.getState(),
                address.getPostCode(),
                address.getNotes().replaceAll("(\r\n|\n)", "<br>")
        );

        Organiser organiser = event.getOrganiser();
        this.organiser = new OrganiserDto(
                organiser.getFirstName(),
                organiser.getLastName(),
                organiser.getEmailAddress()
        );
    }
}