package me.nerminsehic.groupevent.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import me.nerminsehic.groupevent.entity.InviteResponse;

@Data
@ToString
@NoArgsConstructor
public class InviteDto {

    @Valid
    @NotNull
    @JsonProperty("event")
    private EventDto event;

    @Valid
    @NotNull
    @JsonProperty("attendee")
    private AttendeeDto attendee;

    @JsonProperty("response")
    private InviteResponse response;

    @JsonProperty("message")
    private String message;
}
