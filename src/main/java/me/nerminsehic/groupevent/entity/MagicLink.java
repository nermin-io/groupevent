package me.nerminsehic.groupevent.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

@ToString
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Entity(name = "MagicLink")
@Table(name = "magic_link")
public class MagicLink {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    private UUID id;

    @ManyToOne
    @JoinColumn(
            name = "organiser_id",
            referencedColumnName = "id",
            foreignKey = @ForeignKey(
                    name = "magic_link_organiser_id_fk"
            ),
            nullable = false
    )
    private Organiser organiser;

    @Column(
            name = "created_at",
            nullable = false
    )
    private Instant createdAt;

    @Column(
            name = "expires_at",
            nullable = false
    )
    private Instant expiresAt;

    @Enumerated(EnumType.STRING)
    private LinkStatus status;

    public MagicLink(Organiser organiser) {
        this.organiser = organiser;
        this.status = LinkStatus.OPEN;
        this.createdAt = Instant.now();
        this.expiresAt = Instant.now().plus(1, ChronoUnit.HOURS);
    }
}
