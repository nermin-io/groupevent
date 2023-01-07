package me.nerminsehic.groupevent.repository;

import me.nerminsehic.groupevent.entity.Attendee;
import org.assertj.core.api.AssertionsForClassTypes;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;


@DataJpaTest
class AttendeesTest {

    @Autowired
    private Attendees underTest;

    @AfterEach
    void tearDown() {
        underTest.deleteAll();
    }

    @Test
    void itShouldFindAttendeeByEmailAddress() {
        // given
        String emailAddress = "test.test@email.com";
        Attendee attendee = underTest.save(new Attendee(
                emailAddress
        ));

        // when
        Optional<Attendee> result = underTest.findByEmailAddress(emailAddress);

        // then
        assertThat(result).isPresent();
    }

    @Test
    void itShouldNotFindAttendeeByNonExistingEmailAddress() {
        // given
        String nonExistingEmailAddress = "test.test@email.com";

        // when
        Optional<Attendee> result = underTest.findByEmailAddress(nonExistingEmailAddress);

        // then
        assertThat(result).isNotPresent();
    }
}