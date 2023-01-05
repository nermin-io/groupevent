package me.nerminsehic.groupevent.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.Instant;
import java.util.UUID;

@Data
@ToString
@NoArgsConstructor
public class AddressDto {

    @JsonProperty("id")
    private UUID id;

    @JsonProperty("address")
    private String address;

    @JsonProperty("address2")
    private String address2;

    @JsonProperty("city")
    private String city;

    @JsonProperty("state")
    private String state;

    @JsonProperty("post_code")
    private String postCode;

    @JsonProperty("notes")
    private String notes;

    @JsonProperty("created_at")
    private Instant createdAt;

    @JsonProperty("updated_at")
    private Instant updatedAt;

    public AddressDto(String address, String address2, String city, String state, String postCode, String notes) {
        this.address = address;
        this.address2 = address2;
        this.city = city;
        this.state = state;
        this.postCode = postCode;
        this.notes = notes;
    }
}
