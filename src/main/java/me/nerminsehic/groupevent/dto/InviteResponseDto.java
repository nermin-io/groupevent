package me.nerminsehic.groupevent.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.ToString;
import me.nerminsehic.groupevent.entity.InviteResponse;

@Data
@ToString
public class InviteResponseDto {

    @NotBlank(message = "required field")
    @JsonProperty("first_name")
    private String firstName;

    @NotBlank(message = "required field")
    @JsonProperty("last_name")
    private String lastName;

    @NotNull(message = "required field")
    @JsonProperty("response")
    private InviteResponse response;

    @NotBlank(message = "required field")
    @JsonProperty("message")
    private String message;
}
