package me.nerminsehic.groupevent.service;

import me.nerminsehic.groupevent.entity.Address;
import me.nerminsehic.groupevent.entity.Organiser;
import me.nerminsehic.groupevent.exception.IllegalOperationException;
import me.nerminsehic.groupevent.exception.NotFoundException;
import me.nerminsehic.groupevent.repository.Addresses;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class AddressServiceImpl implements AddressService {

    private final OrganiserService organiserService;
    private final Addresses addresses;

    public AddressServiceImpl(OrganiserService organiserService, Addresses addresses) {
        this.organiserService = organiserService;
        this.addresses = addresses;
    }

    @Override
    public Optional<Address> findById(UUID organiserId, UUID addressId) {
        Organiser organiser = organiserService.getOrganiserById(organiserId);
        return addresses.findByIdAndOrganiser(addressId, organiser);
    }

    @Override
    public List<Address> findAll(UUID organiserId) {
        Organiser organiser = organiserService.getOrganiserById(organiserId);
        return addresses.findAllByOrganiser(organiser);
    }

    @Override
    public Address create(UUID organiserId, Address address) {
        Organiser organiser = organiserService.getOrganiserById(organiserId);
        Address newAddress = new Address(
                organiser,
                address.getAddress(),
                address.getAddress2(),
                address.getCity(),
                address.getState(),
                address.getPostCode(),
                address.getNotes()
        );
        newAddress.setUpdatedAt(Instant.now());

        return addresses.save(newAddress);
    }

    @Override
    public void deleteById(UUID organiserId, UUID addressId) {
        Organiser organiser = organiserService.getOrganiserById(organiserId);
        Address address = addresses.findByIdAndOrganiser(addressId, organiser)
                .orElseThrow(() -> new NotFoundException(Address.class, addressId));

        addresses.delete(address);
    }

    @Override
    public void delete(UUID organiserId, Address address) {
        Organiser organiser = organiserService.getOrganiserById(organiserId);
        if(!address.getOrganiser().equals(organiser)) // security check
            throw new IllegalOperationException("Attempt to illegally delete an address failed.");

        addresses.delete(address);
    }

    @Override
    public Address updateById(UUID organiserId, UUID addressId, Address newAddress) {
        Organiser organiser = organiserService.getOrganiserById(organiserId);
        Address currentAddress = addresses.findByIdAndOrganiser(addressId, organiser)
                .orElseThrow(() -> new NotFoundException(Address.class, addressId));

        return updateAddress(currentAddress, newAddress);
    }

    @Override
    public Address findOrCreateAddress(UUID organiserId, Address requestedAddress) {
        Organiser organiser = organiserService.getOrganiserById(organiserId);

        Address address = addresses.findByOrganiserAndAddressAndStateAndPostCode(
                organiser,
                requestedAddress.getAddress(),
                requestedAddress.getState(),
                requestedAddress.getPostCode()
        ).orElseGet(() -> {
            Address newAddress = new Address(organiser, requestedAddress);
            newAddress.setUpdatedAt(Instant.now());

            return newAddress;
        });

        return addresses.save(address);
    }

    private Address updateAddress(Address currentAddress, Address newAddress) {
        currentAddress.setAddress(newAddress.getAddress());
        currentAddress.setAddress2(newAddress.getAddress2());
        currentAddress.setCity(newAddress.getCity());
        currentAddress.setState(newAddress.getState());
        currentAddress.setPostCode(newAddress.getPostCode());
        currentAddress.setNotes(newAddress.getNotes());
        currentAddress.setUpdatedAt(Instant.now());

        return addresses.save(currentAddress);
    }
}
