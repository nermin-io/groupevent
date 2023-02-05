package me.nerminsehic.groupevent.service;

import lombok.RequiredArgsConstructor;
import me.nerminsehic.groupevent.entity.MagicLink;
import me.nerminsehic.groupevent.entity.Organiser;
import me.nerminsehic.groupevent.exception.NotFoundException;
import me.nerminsehic.groupevent.exception.UniqueConstraintException;
import me.nerminsehic.groupevent.repository.Organisers;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;

import java.time.Clock;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrganiserServiceImpl implements OrganiserService {

    private final Organisers organisers;
    private final MagicLinkService magicLinkService;
    private final Clock clock;

    @Override
    public Optional<Organiser> findById(UUID id) {
        return organisers.findById(id);
    }

    @Override
    public List<Organiser> findAll() {
        return organisers.findAll();
    }

    @Override
    public Organiser create(Organiser organiser) {
        Optional<Organiser> existingOrganiserOpt = organisers.findByEmailAddress(organiser.getEmailAddress());
        if (existingOrganiserOpt.isPresent())
            throw new UniqueConstraintException(Organiser.class, "email_address", organiser.getEmailAddress());

        organiser.setUpdatedAt(Instant.now(clock));
        organiser.setCreatedAt(Instant.now(clock));

        return organisers.save(organiser);
    }

    @Override
    public void deleteById(UUID id) {
        organisers.deleteById(id);
    }

    @Override
    public void delete(Organiser organiser) {
        organisers.delete(organiser);
    }

    @Override
    public Organiser updateById(UUID id, Organiser organiser) {
        Organiser currentOrganiser = findById(id)
                .orElseThrow(() -> new NotFoundException(Organiser.class, id));

        return updateOrganiser(currentOrganiser, organiser);
    }

    @Override
    public Organiser getOrganiserById(UUID id) {
        return findById(id)
                .orElseThrow(() -> new NotFoundException(Organiser.class, id));
    }

    @Override
    public Pair<Organiser, Boolean> findOrCreateOrganiser(Organiser organiser) {
        return organisers.findByEmailAddress(organiser.getEmailAddress())
                .map(o -> Pair.of(o, false))
                .orElseGet(() -> {
                    organiser.setUpdatedAt(Instant.now(clock));
                    organiser.setCreatedAt(Instant.now(clock));
                    return Pair.of(organisers.save(organiser), true);
                });
    }

    @Override
    public boolean attemptLogin(Organiser organiser) {
        Pair<Organiser, Boolean> result = findOrCreateOrganiser(organiser);

        Organiser organiserResult = result.getFirst();
        magicLinkService.create(organiserResult);

        return result.getSecond();
    }

    private Organiser updateOrganiser(Organiser currentOrganiser, Organiser newOrganiser) {
        currentOrganiser.setFirstName(newOrganiser.getFirstName());
        currentOrganiser.setLastName(newOrganiser.getLastName());
        currentOrganiser.setEmailAddress(newOrganiser.getEmailAddress());
        currentOrganiser.setUpdatedAt(Instant.now(clock));

        return organisers.save(currentOrganiser);
    }
}
