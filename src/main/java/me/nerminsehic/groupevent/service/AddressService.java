package me.nerminsehic.groupevent.service;


import me.nerminsehic.groupevent.entity.Address;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface AddressService {
    Optional<Address> findById(UUID organiserId, UUID addressId);

    List<Address> findAll(UUID organiserId);

    Address create(UUID organiserId, Address address);

    void deleteById(UUID organiserId, UUID addressId);

    void delete(UUID organiserId, Address address);

    Address updateById(UUID organiserId, UUID addressId, Address entity);

    Address findOrCreateAddress(UUID organiserId, Address address);
}
