package me.nerminsehic.groupevent.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.ToString;
import me.nerminsehic.groupevent.entity.InviteResponse;

@Data
@ToString
public class InviteResponseDto {

    @JsonProperty("first_name")
    private String firstName;

    @JsonProperty("last_name")
    private String lastName;

    @JsonProperty("response")
    private InviteResponse response;

    @JsonProperty("message")
    private String message;
}
