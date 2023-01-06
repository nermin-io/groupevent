package me.nerminsehic.groupevent.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.Instant;
import java.util.UUID;

@Data
@ToString
@NoArgsConstructor
public class AttendeeDto {

    @JsonProperty("id")
    private UUID id;

    @NotBlank(message = "required field")
    @JsonProperty("first_name")
    private String firstName;

    @NotBlank(message = "required field")
    @JsonProperty("last_name")
    private String lastName;

    @NotBlank(message = "required field")
    @Pattern(regexp = "^[a-zA-Z0-9_!#$%&’*+\\/=?`{|}~^.-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z0-9.-]+$", message = "not a valid email address")
    @JsonProperty("email_address")
    private String emailAddress;

    @JsonProperty("created_at")
    private Instant createdAt;

    @JsonProperty("last_invited")
    private Instant lastInvited;

    public AttendeeDto(UUID id, String firstName, String lastName, String emailAddress) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.emailAddress = emailAddress;
    }
}
