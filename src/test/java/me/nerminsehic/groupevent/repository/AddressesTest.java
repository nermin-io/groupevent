package me.nerminsehic.groupevent.repository;

import com.github.javafaker.Faker;
import me.nerminsehic.groupevent.TestingConfig;
import me.nerminsehic.groupevent.entity.Address;
import me.nerminsehic.groupevent.entity.Organiser;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;


import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
@Import(TestingConfig.class)
class AddressesTest {

    @Autowired
    private Addresses underTest;

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
    void itShouldFindAddressByIdAndOrganiser() {
        // given
        Organiser organiser = createTestOrganiser();
        Address address = createTestAddress(organiser);

        // when
        Address foundAddress = underTest.findByIdAndOrganiser(address.getId(), organiser)
                .orElse(null);

        // then
        assertThat(foundAddress).isEqualTo(address);
    }

    @Test
    void itShouldNotFindAddressByIdAndInvalidOrganiser() {
        // given
        Organiser expectedOrganiser = createTestOrganiser();
        Organiser invalidOrganiser = createTestOrganiser();
        Address address = createTestAddress(expectedOrganiser);

        // when
        Optional<Address> result = underTest.findByIdAndOrganiser(address.getId(), invalidOrganiser);

        // then
        assertThat(result).isNotPresent();
    }

    @Test
    void itShouldFindAllAddressesByOrganiser() {
        // given
        Organiser organiser = createTestOrganiser();
        Address address1 = createTestAddress(organiser);
        Address address2 = createTestAddress(organiser);
        Address address3 = createTestAddress(organiser);

        // when
        List<Address> allByOrganiser = underTest.findAllByOrganiser(organiser);

        // then
        assertThat(allByOrganiser.size()).isEqualTo(3);
    }

    @Test
    void itShouldFindAddressByOrganiserAndAddressAndStateAndPostCode() {
        // given
        Organiser organiser = createTestOrganiser();

        String streetAddress = faker.address().streetAddress();
        String state = faker.address().state();
        String postCode = faker.address().zipCode();

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
        Organiser organiser = createTestOrganiser();
        Address address = createTestAddress(organiser);

        // when
        Optional<Address> result = underTest.findByOrganiserAndAddressAndStateAndPostCode(organiser, "456 Fake St", "VIC", "3000");

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

    Address createTestAddress(Organiser organiser) {
        return underTest.save(new Address(
           organiser,
           faker.address().streetAddress(),
           faker.address().secondaryAddress(),
           faker.address().city(),
           faker.address().state(),
           faker.address().zipCode(),
           ""
        ));
    }
}