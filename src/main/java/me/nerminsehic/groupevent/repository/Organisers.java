package me.nerminsehic.groupevent.repository;

import me.nerminsehic.groupevent.entity.Organiser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface Organisers extends JpaRepository<Organiser, UUID> {
    Optional<Organiser> findByEmailAddress(String emailAddress);
}
