package me.nerminsehic.groupevent.entity.key;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;

import java.io.Serializable;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@ToString
@Embeddable
public class InviteCompositeKey implements Serializable {

    @Column(name = "event_id")
    private UUID eventId;

    @Column(name = "attendee_id")
    private UUID attendeeId;
}
