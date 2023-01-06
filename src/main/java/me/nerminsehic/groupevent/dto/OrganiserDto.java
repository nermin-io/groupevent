package me.nerminsehic.groupevent.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import me.nerminsehic.groupevent.entity.Organiser;

import java.time.Instant;
import java.util.UUID;

@Data
@ToString
@NoArgsConstructor
public class OrganiserDto {

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

    @JsonProperty("updated_at")
    private Instant updatedAt;

    public OrganiserDto(String firstName, String lastName, String emailAddress) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.emailAddress = emailAddress;
    }

    public OrganiserDto(Organiser organiser) {
        this.id = organiser.getId();
        this.firstName = organiser.getFirstName();
        this.lastName = organiser.getLastName();
        this.emailAddress = organiser.getEmailAddress();
    }
}