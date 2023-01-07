package me.nerminsehic.groupevent.repository;

import me.nerminsehic.groupevent.entity.Address;
import me.nerminsehic.groupevent.entity.Organiser;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;


import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
class AddressesTest {

    @Autowired
    private Addresses underTest;

    @Autowired
    private Organisers organisers;

    @AfterEach
    void tearDown() {
        underTest.deleteAll();
        organisers.deleteAll();
    }

    @Test
    void itShouldFindAddressByIdAndOrganiser() {
        // given
        Organiser organiser = organisers.save(new Organiser(
                "Test",
                "Test",
                "test.test@email.com"
        ));

        Address address = underTest.save(new Address(
                organiser,
                "123 Fake St",
                "",
                "Melbourne",
                "VIC",
                "3000",
                "Ring bell"
        ));

        // when
        Address foundAddress = underTest.findByIdAndOrganiser(address.getId(), organiser)
                .orElse(null);

        // then
        assertThat(foundAddress).isEqualTo(address);
    }

    @Test
    void itShouldNotFindAddressByIdAndInvalidOrganiser() {
        // given
        Organiser expectedOrganiser = organisers.save(new Organiser(
                "Test",
                "Test",
                "test.test@email.com"
        ));

        Organiser invalidOrganiser = organisers.save(new Organiser(
                "Invalid",
                "Organiser",
                "invalid.organiser@email.com"
        ));

        Address address = underTest.save(new Address(
                expectedOrganiser,
                "123 Fake St",
                "",
                "Melbourne",
                "VIC",
                "3000",
                "Ring bell"
        ));

        // when
        Optional<Address> result = underTest.findByIdAndOrganiser(address.getId(), invalidOrganiser);

        // then
        assertThat(result).isNotPresent();
    }

    @Test
    void itShouldFindAllAddressesByOrganiser() {
        // given
        Organiser organiser = organisers.save(new Organiser(
                "Test",
                "Test",
                "test.test@email.com"
        ));

        Address address1 = underTest.save(new Address(
                organiser,
                "1 Fake St",
                "",
                "Melbourne",
                "VIC",
                "3000",
                "Ring bell"
        ));

        Address address2 = underTest.save(new Address(
                organiser,
                "2 Fake St",
                "",
                "Melbourne",
                "VIC",
                "3000",
                "Ring bell"
        ));

        Address address3 = underTest.save(new Address(
                organiser,
                "3 Fake St",
                "",
                "Melbourne",
                "VIC",
                "3000",
                "Ring bell"
        ));

        // when
        List<Address> allByOrganiser = underTest.findAllByOrganiser(organiser);

        // then
        assertThat(allByOrganiser.size()).isEqualTo(3);
    }

    @Test
    void itShouldFindAddressByOrganiserAndAddressAndStateAndPostCode() {
        // given
        Organiser organiser = organisers.save(new Organiser(
                "Test",
                "Test",
                "test.test@email.com"
        ));

        String streetAddress = "123 Fake St";
        String state = "VIC";
        String postCode = "3000";

        Address address = underTest.save(new Address(
                organiser,
                streetAddress,
                "",
                "-",
                state,
                postCode,
                ""
        ));
        // when
        Optional<Address> result = underTest.findByOrganiserAndAddressAndStateAndPostCode(organiser, streetAddress, state, postCode);

        // then
        assertThat(result).isPresent();
    }

    @Test
    void itShouldNotFindAddressByOrganiserAndInvalidAddressAndStateAndPostCode() {
        // given
        Organiser organiser = organisers.save(new Organiser(
                "Test",
                "Test",
                "test.test@email.com"
        ));

        Address address = underTest.save(new Address(
                organiser,
                "123 Fake St",
                "",
                "Melbourne",
                "VIC",
                "3000",
                ""
        ));

        // when
        Optional<Address> result = underTest.findByOrganiserAndAddressAndStateAndPostCode(organiser, "456 Fake St", "VIC", "3000");

        // then
        assertThat(result).isNotPresent();
    }
}