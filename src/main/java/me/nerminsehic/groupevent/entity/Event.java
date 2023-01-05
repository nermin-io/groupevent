package me.nerminsehic.groupevent.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@ToString
@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "Event")
@Table(
        name = "event"
)
public class Event {

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
                    name = "event_organiser_id_fk"
            ),
            nullable = false
    )
    private Organiser organiser;

    @ManyToOne
    @JoinColumn(
            name = "address_id",
            referencedColumnName = "id",
            foreignKey = @ForeignKey(
                    name = "event_address_id_fk"
            ),
            nullable = false
    )
    private Address address;

    @Column(
            name = "name",
            nullable = false,
            columnDefinition = "TEXT"
    )
    private String name;

    @Column(
            name = "description",
            columnDefinition = "TEXT"
    )
    private String description;

    @Column(
            name = "scheduled_date",
            columnDefinition = "DATE",
            nullable = false
    )
    private LocalDate scheduledDate;

    @Column(
            name = "time_from",
            columnDefinition = "TIME WITHOUT TIME ZONE",
            nullable = false
    )
    private LocalTime timeFrom;

    @Column(
            name = "time_to",
            columnDefinition = "TIME WITHOUT TIME ZONE",
            nullable = false
    )
    private LocalTime timeTo;

    @Column(
            name = "agenda",
            columnDefinition = "TEXT"
    )
    private String agenda;

    @Column(
            name = "status",
            nullable = false
    )
    @Enumerated(EnumType.STRING)
    private EventStatus status = EventStatus.DRAFT;

    @Column(
            name = "cancel_message",
            columnDefinition = "TEXT"
    )
    private String cancelMessage;

    @OneToMany(
            mappedBy = "event",
            orphanRemoval = true,
            cascade = { CascadeType.PERSIST, CascadeType.REMOVE, CascadeType.REFRESH }
    )
    private Set<Invite> invites = new HashSet<>();

    @Column(
            name = "created_at",
            nullable = false
    )
    private Instant createdAt;

    @Column(
            name = "updated_at"
    )
    private Instant updatedAt;

    public Event(
            Organiser organiser,
            Address address,
            Set<Attendee> attendees,
            String name,
            String description,
            LocalDate scheduledDate,
            LocalTime timeFrom,
            LocalTime timeTo,
            String agenda
    ) {
       this.organiser = organiser;
       this.address = address;
       this.name = name;
       this.description = description;
       this.scheduledDate = scheduledDate;
       this.timeFrom = timeFrom;
       this.timeTo = timeTo;
       this.agenda = agenda;
       this.invites = attendees
               .stream()
               .map(attendee -> new Invite(this, attendee))
               .collect(Collectors.toSet());

       this.createdAt = Instant.now();
    }
}
