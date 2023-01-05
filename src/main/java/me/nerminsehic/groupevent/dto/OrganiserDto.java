package me.nerminsehic.groupevent.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
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

    @JsonProperty("first_name")
    private String firstName;

    @JsonProperty("last_name")
    private String lastName;

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