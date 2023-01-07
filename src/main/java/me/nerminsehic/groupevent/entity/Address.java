package me.nerminsehic.groupevent.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import java.time.Instant;
import java.util.UUID;

@ToString
@Getter
@Setter
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "Address")
@Table(
        name = "address",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "address_organiser_unique",
                        columnNames = {"organiser_id", "address", "post_code", "state"}
                )
        }
)
public class Address {
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
                    name = "address_organiser_id_fk"
            ),
            nullable = false
    )
    private Organiser organiser;

    @Column(
            name = "address",
            nullable = false,
            columnDefinition = "TEXT"
    )
    private String address;

    @Column(
            name = "address2",
            columnDefinition = "TEXT"
    )
    private String address2;

    @Column(
            name = "city",
            nullable = false,
            columnDefinition = "TEXT"
    )
    private String city;

    @Column(
            name = "state",
            nullable = false,
            columnDefinition = "VARCHAR(20)"
    )
    private String state;

    @Column(
            name = "post_code",
            nullable = false,
            columnDefinition = "VARCHAR(20)"
    )
    private String postCode;

    @Column(
            name = "notes",
            columnDefinition = "TEXT"
    )
    private String notes;

    @Column(
            name = "created_at",
            nullable = false
    )
    private Instant createdAt;

    @Column(
            name = "updated_at"
    )
    private Instant updatedAt;

    public String getFormatted() {
        return "%s, %s, %s %s, %s".formatted(
                address,
                city,
                state,
                postCode,
                "AU"
        );
    }

    public Address(Organiser organiser, String address, String address2, String city, String state, String postCode, String notes) {
        this.organiser = organiser;
        this.address = address;
        this.address2 = address2;
        this.city = city;
        this.state = state;
        this.postCode = postCode;
        this.notes = notes;
        this.createdAt = Instant.now();
        this.updatedAt = Instant.now();
    }

    public Address(Organiser organiser, Address eventAddress) {
        this.organiser = organiser;
        this.address = eventAddress.getAddress();
        this.address2 = eventAddress.getAddress2();
        this.city = eventAddress.getCity();
        this.state = eventAddress.getState();
        this.postCode = eventAddress.getPostCode();
        this.notes = eventAddress.getNotes();
        this.createdAt = Instant.now();
    }
}
