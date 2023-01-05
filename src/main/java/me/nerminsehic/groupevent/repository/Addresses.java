package me.nerminsehic.groupevent.repository;

import me.nerminsehic.groupevent.entity.Address;
import me.nerminsehic.groupevent.entity.Organiser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface Addresses extends JpaRepository<Address, UUID> {

    Optional<Address> findByIdAndOrganiser(UUID id, Organiser organiser);

    List<Address> findAllByOrganiser(Organiser organiser);

    Optional<Address> findByOrganiserAndAddressAndStateAndPostCode(Organiser organiser, String address, String state, String postCode);

}
