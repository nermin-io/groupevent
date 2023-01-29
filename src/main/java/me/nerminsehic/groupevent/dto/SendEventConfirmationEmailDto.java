package me.nerminsehic.groupevent.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import me.nerminsehic.groupevent.entity.Address;
import me.nerminsehic.groupevent.entity.Event;
import me.nerminsehic.groupevent.entity.Organiser;

import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

@Data
@ToString
@NoArgsConstructor
public class SendEventConfirmationEmailDto {

    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("EEEE, MMM dd, yyyy");
    private static final DateTimeFormatter TIME_FORMAT = DateTimeFormatter.ofPattern("hh:mm a");

    @JsonProperty("event_id")
    private UUID eventId;

    @JsonProperty("address")
    private AddressDto address;

    @JsonProperty("name")
    private String name;

    @JsonProperty("description")
    private String description;

    @JsonProperty("scheduled_date")
    private String scheduledDate;

    @JsonProperty("time_from")
    private String timeFrom;

    @JsonProperty("time_to")
    private String timeTo;

    @JsonProperty("agenda")
    private String agenda;

    @JsonProperty("duration_in_hours")
    private long durationInHours;

    @JsonProperty("organiser")
    private OrganiserDto organiser;

    @JsonProperty("map_link")
    private String mapLink;

    @JsonProperty("directions_link")
    private String directionsLink;

    public SendEventConfirmationEmailDto(Event event, String mapLink, String directionsLink) {
        this.eventId = event.getId();
        this.name = event.getName();
        this.description = event.getDescription();
        this.agenda = event.getAgenda();
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
                address.getNotes()
        );

        Organiser organiser = event.getOrganiser();
        this.organiser = new OrganiserDto(
                organiser.getFirstName(),
                organiser.getLastName(),
                organiser.getEmailAddress()
        );
    }
}
