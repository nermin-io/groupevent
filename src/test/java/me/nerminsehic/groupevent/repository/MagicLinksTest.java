package me.nerminsehic.groupevent.repository;

import com.github.javafaker.Faker;
import me.nerminsehic.groupevent.TestingConfig;
import me.nerminsehic.groupevent.entity.MagicLink;
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
class MagicLinksTest {

    @Autowired
    private MagicLinks underTest;

    @Autowired
    private Organisers organisers;

    @Autowired
    private Faker faker;

    @AfterEach
    void tearDown() {
        underTest.deleteAll();
        organisers.deleteAll();
    }

    @Test
    void itShouldFindLinkByIdAndOrganiser() {
        // given
        Organiser organiser = createTestOrganiser();
        MagicLink link = createTestLink(organiser);

        // when
        MagicLink result = underTest.findByIdAndOrganiser(link.getId(), organiser)
                .orElse(null);

        // then
        assertThat(result).isEqualTo(link);
    }

    @Test
    void itShouldNotFindLinkByIdAndInvalidOrganiser() {
        // given
        Organiser expectedOrganiser = createTestOrganiser();
        Organiser invalidOrganiser = createTestOrganiser();
        MagicLink link = createTestLink(expectedOrganiser);

        // when
        Optional<MagicLink> result = underTest.findByIdAndOrganiser(link.getId(), invalidOrganiser);

        // then
        assertThat(result).isNotPresent();
    }


    Organiser createTestOrganiser() {
        return organisers.save(new Organiser(
                faker.name().firstName(),
                faker.name().lastName(),
                faker.internet().emailAddress()
        ));
    }

    MagicLink createTestLink(Organiser organiser) {
        return underTest.save(new MagicLink(
                organiser
        ));
    }
}