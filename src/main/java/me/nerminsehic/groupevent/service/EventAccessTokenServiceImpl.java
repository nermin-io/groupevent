package me.nerminsehic.groupevent.service;

import lombok.RequiredArgsConstructor;
import me.nerminsehic.groupevent.entity.Event;
import me.nerminsehic.groupevent.entity.Organiser;
import me.nerminsehic.groupevent.exception.IllegalAccessTokenException;
import me.nerminsehic.groupevent.repository.Events;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class EventAccessTokenServiceImpl implements EventAccessTokenService {

    private final Events events;
    private final OrganiserService organiserService;
    private final EncryptionService encryptionService;
    private static final String DELIMITER = "|";

    @Override
    public String createToken(Event event) {
        String value = "%s%s%s".formatted(event.getId(), DELIMITER, event.getOrganiser().getId());
        return encryptionService.encrypt(value);
    }

    @Override
    public Event verifyToken(String key) {
        String decryptedValue = encryptionService.decrypt(key);
        Pair<UUID, UUID> result = parseDecryptedValue(decryptedValue);

        Organiser organiser = organiserService.getOrganiserById(result.getSecond());
        return events.findByIdAndOrganiser(result.getFirst(), organiser)
                .orElseThrow(() -> new IllegalAccessTokenException("Invalid access token"));

    }

    private Pair<UUID, UUID> parseDecryptedValue(String val) {
        if(!val.contains(DELIMITER))
            throw new IllegalAccessTokenException("Invalid access token");

        String[] values = val.split("\\%s".formatted(DELIMITER));

        try {
            UUID eventId = UUID.fromString(values[0]);
            UUID organiserId = UUID.fromString(values[1]);

            return Pair.of(eventId, organiserId);
        } catch(IllegalArgumentException e) {
            throw new IllegalAccessTokenException("Could not evaluate UUID");
        }


    }
}
