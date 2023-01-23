package me.nerminsehic.groupevent.service;

import me.nerminsehic.groupevent.entity.Organiser;
import org.springframework.data.util.Pair;

import java.util.UUID;

public interface OrganiserService extends Service<Organiser, UUID> {

    Organiser getOrganiserById(UUID id);

    Pair<Organiser, Boolean> findOrCreateOrganiser(Organiser organiser);

    boolean attemptLogin(Organiser organiser);
}
