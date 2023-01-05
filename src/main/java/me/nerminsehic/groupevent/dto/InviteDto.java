package me.nerminsehic.groupevent.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString
@NoArgsConstructor
public class InviteDto {

    @JsonProperty("event")
    private EventDto event;

    @JsonProperty("attendee")
    private AttendeeDto attendee;
}
