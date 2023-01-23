package me.nerminsehic.groupevent.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

import java.util.UUID;

@Data
@ToString
@AllArgsConstructor
public class SendMagicLinkDto {

    @JsonProperty("organiser_id")
    private UUID organiserId;

    @JsonProperty("first_name")
    private String firstName;

    @JsonProperty("last_name")
    private String lastName;

    @JsonProperty("token")
    private UUID token;
}
