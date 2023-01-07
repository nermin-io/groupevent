package me.nerminsehic.groupevent.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import java.time.Instant;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Entity(name = "Attendee")
@Table(
        name = "attendee",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "attendee_email_address_unique",
                        columnNames = "email_address"
                )
        }
)
public class Attendee {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    private UUID id;

    @Column(
            name = "first_name",
            columnDefinition = "TEXT"
    )
    private String firstName;

    @Column(
            name = "last_name",
            columnDefinition = "TEXT"
    )
    private String lastName;

    @Column(
            name = "email_address",
            nullable = false,
            columnDefinition = "TEXT"
    )
    private String emailAddress;

    @Column(
            name = "created_at",
            nullable = false
    )
    @EqualsAndHashCode.Exclude
    private Instant createdAt;

    @Column(
            name = "last_invited"
    )
    private Instant lastInvited;

    public Attendee(String emailAddress) {
        this.emailAddress = emailAddress;
        this.createdAt = Instant.now();
    }

    @Override
    public String toString() {
        return emailAddress;
    }
}
