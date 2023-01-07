package me.nerminsehic.groupevent.repository;

import com.github.javafaker.Faker;
import me.nerminsehic.groupevent.entity.Address;
import me.nerminsehic.groupevent.entity.Event;
import me.nerminsehic.groupevent.entity.Organiser;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
class EventsTest {

    @Autowired
    private Events underTest;

    @Autowired
    private Organisers organisers;

    @Autowired
    private Addresses addresses;

    private final Faker faker = new Faker();

    @AfterEach
    void tearDown() {
        underTest.deleteAll();
        organisers.deleteAll();
        addresses.deleteAll();
    }

    @Test
    void itShould_FindEventByIdAndOrganiser() {
        // given
        Organiser organiser = createTestOrganiser();
        Event event = createTestEvent(organiser);

        // when
        Event result = underTest.findByIdAndOrganiser(event.getId(), organiser)
                .orElse(null);

        // then
        assertThat(result).isEqualTo(event);
    }

    @Test
    void itShouldNot_FindEventByIdAndOrganiser_WhenInvalidOrganiserProvided() {
        // given
        Organiser expectedOrganiser = createTestOrganiser();
        Organiser invalidOrganiser = createTestOrganiser();
        Event event = createTestEvent(expectedOrganiser);

        // when
        Optional<Event> result = underTest.findByIdAndOrganiser(event.getId(), invalidOrganiser);

        // then
        assertThat(result).isNotPresent();
    }

    @Test
    void itShould_FindAllEventsByOrganiser() {
        // given
        Organiser organiser = createTestOrganiser();
        Event event1 = createTestEvent(organiser);
        Event event2 = createTestEvent(organiser);
        Event event3 = createTestEvent(organiser);

        // when
        List<Event> allEventsByOrganiser = underTest.findAllByOrganiser(organiser);

        // then
        assertThat(allEventsByOrganiser.size()).isEqualTo(3);
    }

    Event createTestEvent(Organiser organiser) {
        Address address = addresses.save(new Address(
                organiser,
                faker.address().streetAddress(),
                faker.address().secondaryAddress(),
                faker.address().city(),
                faker.address().state(),
                faker.address().zipCode(),
                "Ring bell"
        ));

        return underTest.save(new Event(
                organiser,
                address,
                Collections.emptySet(),
                "Test Event",
                "Lorem Ipsum",
                LocalDate.now(),
                LocalTime.now(),
                LocalTime.now(),
                "Agenda"
        ));
    }

    Organiser createTestOrganiser() {
        return organisers.save(new Organiser(
           faker.name().firstName(),
           faker.name().lastName(),
           faker.internet().emailAddress()
        ));
    }
}