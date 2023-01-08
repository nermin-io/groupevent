package me.nerminsehic.groupevent.service;

import com.github.javafaker.Faker;
import me.nerminsehic.groupevent.entity.Address;
import me.nerminsehic.groupevent.entity.Organiser;
import me.nerminsehic.groupevent.exception.IllegalOperationException;
import me.nerminsehic.groupevent.exception.NotFoundException;
import me.nerminsehic.groupevent.repository.Addresses;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Clock;
import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;


@ExtendWith(MockitoExtension.class)
class AddressServiceImplTest {

    @Mock
    private OrganiserService organiserService;

    @Mock
    private Addresses addresses;

    @Mock
    private Clock clock;

    private AddressService underTest;
    private final Faker faker = new Faker();

    @BeforeEach
    void setUp() {
        underTest = new AddressServiceImpl(organiserService, addresses, clock);
    }

    @Test
    void findById_ItShould_FindAddressByOrganiserAndAddressId() {
        // given
        UUID organiserId = UUID.randomUUID();
        UUID addressId = UUID.randomUUID();
        Organiser organiser = createTestOrganiser();

        given(organiserService.getOrganiserById(organiserId))
                .willReturn(organiser);

        // when
        underTest.findById(organiserId, addressId);

        // then
        ArgumentCaptor<UUID> uuidArgumentCaptor = ArgumentCaptor.forClass(UUID.class);
        ArgumentCaptor<Organiser> organiserArgumentCaptor = ArgumentCaptor.forClass(Organiser.class);
        verify(addresses).findByIdAndOrganiser(uuidArgumentCaptor.capture(), organiserArgumentCaptor.capture());

        UUID addressIdArg = uuidArgumentCaptor.getValue();
        Organiser organiserArg = organiserArgumentCaptor.getValue();

        assertThat(addressIdArg).isEqualTo(addressId);
        assertThat(organiserArg).isEqualTo(organiser);
    }

    @Test
    void findAll_ItShould_FindAllAddresses_GivenOrganiserId() {
        // given
        UUID organiserId = UUID.randomUUID();
        Organiser organiser = createTestOrganiser();

        given(organiserService.getOrganiserById(organiserId))
                .willReturn(organiser);
        // when
        underTest.findAll(organiserId);

        // then
        ArgumentCaptor<Organiser> organiserArgumentCaptor = ArgumentCaptor.forClass(Organiser.class);
        verify(addresses).findAllByOrganiser(organiserArgumentCaptor.capture());

        Organiser organiserArg = organiserArgumentCaptor.getValue();
        assertThat(organiserArg).isEqualTo(organiser);
    }

    @Test
    void create_ItShould_CreateNewAddress_GivenOrganiserIdAndAddress() {
        // given
        UUID organiserId = UUID.randomUUID();
        Organiser organiser = createTestOrganiser();
        Address address = new Address(
                null,
                faker.address().streetAddress(),
                faker.address().secondaryAddress(),
                faker.address().city(),
                faker.address().state(),
                faker.address().zipCode(),
                "Ring Bell"
        );

        given(organiserService.getOrganiserById(organiserId))
                .willReturn(organiser);

        // when
        underTest.create(organiserId, address);
        address.setOrganiser(organiser);
        address.setUpdatedAt(Instant.now(clock));

        // then
        ArgumentCaptor<Address> addressArgumentCaptor = ArgumentCaptor.forClass(Address.class);
        verify(addresses).save(addressArgumentCaptor.capture());

        Address addressArg = addressArgumentCaptor.getValue();
        assertThat(addressArg).isEqualTo(address);
    }

    @Test
    void deleteById_ItShould_DeleteAddressById_GivenOrganiserId() {
        // given
        UUID organiserId = UUID.randomUUID();
        UUID addressId = UUID.randomUUID();
        Organiser organiser = createTestOrganiser();
        Address address = createTestAddress(organiser);

        given(organiserService.getOrganiserById(organiserId))
                .willReturn(organiser);

        given(addresses.findByIdAndOrganiser(addressId, organiser))
                .willReturn(Optional.of(address));
        // when
        underTest.deleteById(organiserId, addressId);

        // then
        ArgumentCaptor<Address> addressArgumentCaptor = ArgumentCaptor.forClass(Address.class);
        verify(addresses).delete(addressArgumentCaptor.capture());

        Address addressArg = addressArgumentCaptor.getValue();
        assertThat(addressArg).isEqualTo(address);
    }

    @Test
    void deleteById_ItShouldThrow_NotFoundException_WhenAddressNotExists() {
        // given
        UUID organiserId = UUID.randomUUID();
        UUID addressId = UUID.randomUUID();
        Organiser organiser = createTestOrganiser();

        given(organiserService.getOrganiserById(organiserId))
                .willReturn(organiser);

        given(addresses.findByIdAndOrganiser(addressId, organiser))
                .willReturn(Optional.empty());

        // when
        // then
        assertThatThrownBy(() -> underTest.deleteById(organiserId, addressId))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("Cannot find");
    }

    @Test
    void delete_ItShould_DeleteAddress_GivenOrganiserIdAndAddress() {
        // given
        UUID organiserId = UUID.randomUUID();
        Organiser organiser = createTestOrganiser();
        Address address = createTestAddress(organiser);

        given(organiserService.getOrganiserById(organiserId))
                .willReturn(organiser);

        // when
        underTest.delete(organiserId, address);

        // then
        ArgumentCaptor<Address> addressArgumentCaptor = ArgumentCaptor.forClass(Address.class);
        verify(addresses).delete(addressArgumentCaptor.capture());

        Address addressArg = addressArgumentCaptor.getValue();
        assertThat(addressArg).isEqualTo(address);
    }

    @Test
    void delete_ItShouldThrow_IllegalOperationException_WhenInvalidOrganiserIsProvided() {
        // given
        UUID organiserId = UUID.randomUUID();
        Organiser validOrganiser = createTestOrganiser();
        Organiser invalidOrganiser = createTestOrganiser();
        Address address = createTestAddress(validOrganiser);

        given(organiserService.getOrganiserById(organiserId))
                .willReturn(invalidOrganiser);

        // when
        // then
        assertThatThrownBy(() -> underTest.delete(organiserId, address))
                .isInstanceOf(IllegalOperationException.class)
                .hasMessageContaining("Attempt to illegally delete an address failed");
    }

    @Test
    void updateById_ItShould_UpdateAddressById_GivenOrganiserAndNewAddress() {
        // given
        UUID organiserId = UUID.randomUUID();
        UUID addressId = UUID.randomUUID();
        Organiser organiser = createTestOrganiser();
        Address newAddress = createTestAddress(organiser);

        Address currentAddress = createTestAddress(organiser);
        currentAddress.setCreatedAt(Instant.now(clock));

        Address verificationAddress = createTestAddress(organiser);
        verificationAddress.setCreatedAt(Instant.now(clock));

        given(organiserService.getOrganiserById(organiserId))
                .willReturn(organiser);

        given(addresses.findByIdAndOrganiser(addressId, organiser))
                .willReturn(Optional.of(currentAddress));

        // when
        underTest.updateById(organiserId, addressId, newAddress);
        verificationAddress.setAddress(newAddress.getAddress());
        verificationAddress.setAddress2(newAddress.getAddress2());
        verificationAddress.setCity(newAddress.getCity());
        verificationAddress.setState(newAddress.getState());
        verificationAddress.setPostCode(newAddress.getPostCode());
        verificationAddress.setNotes(newAddress.getNotes());
        verificationAddress.setUpdatedAt(Instant.now(clock));

        // then
        ArgumentCaptor<Address> addressArgumentCaptor = ArgumentCaptor.forClass(Address.class);
        verify(addresses).save(addressArgumentCaptor.capture());

        Address addressArg = addressArgumentCaptor.getValue();
        assertThat(addressArg).isEqualTo(verificationAddress);
    }

    @Test
    void updateById_ItShouldThrow_NotFoundException_WhenAddressNotExists() {
        // given
        UUID organiserId = UUID.randomUUID();
        UUID addressId = UUID.randomUUID();
        Organiser organiser = createTestOrganiser();
        Address newAddress = createTestAddress(organiser);

        given(organiserService.getOrganiserById(organiserId))
                .willReturn(organiser);

        given(addresses.findByIdAndOrganiser(addressId, organiser))
                .willReturn(Optional.empty());

        // when
        // then
        assertThatThrownBy(() -> underTest.updateById(organiserId, addressId, newAddress))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("Cannot find");
    }

    @Test
    void findOrCreateAddress_ItShould_FindAddress_WhenExists() {
        // given
        UUID organiserId = UUID.randomUUID();
        Organiser organiser = createTestOrganiser();
        Address address = createTestAddress(organiser);

        given(organiserService.getOrganiserById(organiserId))
                .willReturn(organiser);

        given(addresses.findByOrganiserAndAddressAndStateAndPostCode(
                organiser,
                address.getAddress(),
                address.getState(),
                address.getPostCode()
        )).willReturn(Optional.of(address));

        // when
        Address result = underTest.findOrCreateAddress(organiserId, address);

        // then
        assertThat(result).isEqualTo(address);
    }

    @Test
    void findOrCreateAddress_ItShould_CreateNewAddress_WhenNotExists() {
        // given
        UUID organiserId = UUID.randomUUID();
        Organiser organiser = createTestOrganiser();
        Address address = createTestAddress(organiser);

        given(organiserService.getOrganiserById(organiserId))
                .willReturn(organiser);

        given(addresses.findByOrganiserAndAddressAndStateAndPostCode(
                organiser,
                address.getAddress(),
                address.getState(),
                address.getPostCode()
        )).willReturn(Optional.empty());

        // when
        underTest.findOrCreateAddress(organiserId, address);
        Address newAddress = new Address(organiser, address);
        newAddress.setCreatedAt(Instant.now(clock));
        newAddress.setUpdatedAt(Instant.now(clock));

        // then
        ArgumentCaptor<Address> addressArgumentCaptor = ArgumentCaptor.forClass(Address.class);
        verify(addresses).save(addressArgumentCaptor.capture());

        Address addressArg = addressArgumentCaptor.getValue();
        assertThat(addressArg).isEqualTo(newAddress);

    }

    private Organiser createTestOrganiser() {
        return new Organiser(
                faker.name().firstName(),
                faker.name().lastName(),
                faker.internet().emailAddress()
        );
    }

    private Address createTestAddress(Organiser organiser) {
        return new Address(
          organiser,
          faker.address().streetAddress(),
          faker.address().secondaryAddress(),
          faker.address().city(),
          faker.address().state(),
          faker.address().zipCode(),
          "Ring bell"
        );
    }
}