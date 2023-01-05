package me.nerminsehic.groupevent.entity;

import jakarta.persistence.*;
import lombok.*;
import me.nerminsehic.groupevent.entity.key.InviteCompositeKey;

import java.time.Instant;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity(name = "Invite")
@Table(name = "invite")
public class Invite {

    @EmbeddedId
    private InviteCompositeKey id;

    @ManyToOne
    @MapsId("eventId")
    @JoinColumn(
            name = "event_id",
            foreignKey = @ForeignKey(
                    name = "invite_event_fk"
            )
    )
    private Event event;

    @ManyToOne
    @MapsId("attendeeId")
    @JoinColumn(
            name = "attendee_id",
            foreignKey = @ForeignKey(
                    name = "invite_attendee_fk"
            )
    )
    private Attendee attendee;

    @Column(
            name = "response",
            nullable = false
    )
    @Enumerated(EnumType.STRING)
    private InviteResponse response = InviteResponse.NOT_RESPONDED;

    @Column(
            name = "message",
            columnDefinition = "TEXT"
    )
    private String message;

    @Column(
            name = "created_at",
            nullable = false
    )
    private Instant createdAt;

    @Column(
            name = "updated_at"
    )
    private Instant updatedAt;

    public Invite(Event event, Attendee attendee) {
        this.event = event;
        this.attendee = attendee;
        this.id = new InviteCompositeKey(event.getId(), attendee.getId());
        this.createdAt = Instant.now();
        this.updatedAt = Instant.now();
    }

    @Override
    public String toString() {
        return attendee == null ? null : attendee.toString();
    }
}
