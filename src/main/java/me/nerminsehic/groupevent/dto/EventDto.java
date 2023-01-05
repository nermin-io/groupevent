package me.nerminsehic.groupevent.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import me.nerminsehic.groupevent.entity.Event;
import me.nerminsehic.groupevent.entity.EventStatus;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Data
@ToString
@NoArgsConstructor
public class EventDto {

    @JsonProperty("id")
    private UUID id;

    @JsonProperty("address")
    private AddressDto address;

    @JsonProperty("name")
    private String name;

    @JsonProperty("description")
    private String description;

    @JsonProperty("scheduled_date")
    private LocalDate scheduledDate;

    @JsonProperty("time_from")
    private LocalTime timeFrom;

    @JsonProperty("time_to")
    private LocalTime timeTo;

    @JsonProperty("agenda")
    private String agenda;

    @JsonProperty("status")
    private EventStatus status;

    @JsonProperty("attendees")
    private Set<String> attendees = new HashSet<>();

    @JsonProperty("created_at")
    private Instant createdAt;

    @JsonProperty("updated_at")
    private Instant updatedAt;

    public EventDto(Event event) {
        this.id = event.getId();
        this.name = event.getName();
        this.description = event.getDescription();
        this.scheduledDate = event.getScheduledDate();
        this.timeFrom = event.getTimeFrom();
        this.timeTo = event.getTimeTo();
        this.agenda = event.getAgenda();
        this.status = event.getStatus();
    }
}