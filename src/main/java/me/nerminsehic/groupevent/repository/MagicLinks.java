package me.nerminsehic.groupevent.repository;

import me.nerminsehic.groupevent.entity.MagicLink;
import me.nerminsehic.groupevent.entity.Organiser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface MagicLinks extends JpaRepository<MagicLink, UUID> {

    Optional<MagicLink> findByIdAndOrganiser(UUID id, Organiser organiser);
}
