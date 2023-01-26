package me.nerminsehic.groupevent.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
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

    @NotNull
    @Valid
    @JsonProperty("address")
    private AddressDto address;

    @JsonProperty("organiser")
    private OrganiserDto organiser;

    @NotBlank(message = "required field")
    @JsonProperty("name")
    private String name;

    @NotBlank(message = "required field")
    @JsonProperty("description")
    private String description;

    @NotNull(message = "required field")
    @JsonProperty("scheduled_date")
    private LocalDate scheduledDate;

    @NotNull(message = "required field")
    @JsonProperty("time_from")
    private LocalTime timeFrom;

    @NotNull(message = "required field")
    @JsonProperty("time_to")
    private LocalTime timeTo;

    @NotBlank(message = "required field")
    @JsonProperty("agenda")
    private String agenda;

    @JsonProperty("status")
    private EventStatus status;

    @NotNull(message = "required field")
    @Size(min = 1, max = 300, message = "Event must have between 1 - 300 attendees")
    @JsonProperty("attendees")
    private Set<
            @NotNull
            @Pattern(regexp = "^[a-zA-Z0-9_!#$%&’*+\\/=?`{|}~^.-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z0-9.-]+$", message = "not a valid email address")
                    String> attendees = new HashSet<>();

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