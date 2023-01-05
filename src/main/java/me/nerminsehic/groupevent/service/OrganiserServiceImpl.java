package me.nerminsehic.groupevent.service;

import me.nerminsehic.groupevent.entity.Organiser;
import me.nerminsehic.groupevent.exception.NotFoundException;
import me.nerminsehic.groupevent.exception.UniqueConstraintException;
import me.nerminsehic.groupevent.repository.Organisers;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class OrganiserServiceImpl implements OrganiserService {

    private final Organisers organisers;

    public OrganiserServiceImpl(Organisers organisers) {
        this.organisers = organisers;
    }

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

        Organiser newOrganiser = new Organiser(
                organiser.getFirstName(),
                organiser.getLastName(),
                organiser.getEmailAddress()
        );
        newOrganiser.setUpdatedAt(Instant.now());

        return organisers.save(newOrganiser);
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
                    Organiser newOrganiser = new Organiser(
                            organiser.getFirstName(),
                            organiser.getLastName(),
                            organiser.getEmailAddress()
                    );
                    newOrganiser.setUpdatedAt(Instant.now());

                    return Pair.of(organisers.save(newOrganiser), true);
                });
    }

    private Organiser updateOrganiser(Organiser currentOrganiser, Organiser newOrganiser) {
        currentOrganiser.setFirstName(newOrganiser.getFirstName());
        currentOrganiser.setLastName(newOrganiser.getLastName());
        currentOrganiser.setEmailAddress(newOrganiser.getEmailAddress());
        currentOrganiser.setUpdatedAt(Instant.now());

        return organisers.save(currentOrganiser);
    }
}
