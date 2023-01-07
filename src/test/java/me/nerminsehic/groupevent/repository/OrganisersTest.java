package me.nerminsehic.groupevent.repository;

import com.github.javafaker.Faker;
import me.nerminsehic.groupevent.TestingConfig;
import me.nerminsehic.groupevent.entity.Organiser;
import org.assertj.core.api.AssertionsForClassTypes;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@Import(TestingConfig.class)
class OrganisersTest {

    @Autowired
    private Organisers underTest;

    @Autowired
    private Faker faker;

    @AfterEach
    void tearDown() {
        underTest.deleteAll();
    }

    @Test
    void itShouldFindOrganiserByEmailAddress() {
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
    void itShouldNotFindOrganiserByNonExistingEmailAddress() {
        // given
        String nonExistingEmail = faker.internet().emailAddress();

        // when
        Optional<Organiser> result = underTest.findByEmailAddress(nonExistingEmail);

        // then
        assertThat(result).isNotPresent();
    }
}