package me.nerminsehic.groupevent.repository;

import me.nerminsehic.groupevent.entity.Event;
import me.nerminsehic.groupevent.entity.Organiser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface Events extends JpaRepository<Event, UUID> {

    Optional<Event> findByIdAndOrganiser(UUID id, Organiser organiser);

    List<Event> findAllByOrganiser(Organiser organiser);
}
