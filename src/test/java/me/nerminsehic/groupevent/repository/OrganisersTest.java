package me.nerminsehic.groupevent.repository;

import com.github.javafaker.Faker;
import me.nerminsehic.groupevent.entity.Organiser;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
class OrganisersTest {

    @Autowired
    private Organisers underTest;

    private final Faker faker = new Faker();

    @AfterEach
    void tearDown() {
        underTest.deleteAll();
    }

    @Test
    void findByEmailAddress_ItShould_FindOrganiserByEmailAddress() {
        // given
        String emailAddress = faker.internet().emailAddress();
        Organiser organiser = underTest.save(new Organiser(
           faker.name().firstName(),
           faker.name().lastName(),
           emailAddress
        ));

        // when
        Organiser result = underTest.findByEmailAddress(emailAddress)
                .orElse(null);

        // then
        assertThat(result).isEqualTo(organiser);
    }

    @Test
    void findByEmailAddress_ItShouldNot_FindOrganiserByEmailAddress_WhenEmailNotExists() {
        // given
        String nonExistingEmail = faker.internet().emailAddress();

        // when
        Optional<Organiser> result = underTest.findByEmailAddress(nonExistingEmail);

        // then
        assertThat(result).isNotPresent();
    }
}