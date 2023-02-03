package me.nerminsehic.groupevent.service;

import lombok.RequiredArgsConstructor;
import me.nerminsehic.groupevent.entity.Event;
import me.nerminsehic.groupevent.entity.Organiser;
import me.nerminsehic.groupevent.exception.IllegalAccessTokenException;
import me.nerminsehic.groupevent.repository.Events;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
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
        return encryptionService.encryptString(value);
    }

    @Override
    public Event verifyToken(String key) {
        String decryptedValue = encryptionService.decryptString(key);
        Pair<UUID, UUID> result = parseDecryptedValue(decryptedValue);

        Organiser organiser = organiserService.getOrganiserById(result.getSecond());
        return events.findByIdAndOrganiser(result.getFirst(), organiser)
                .orElseThrow(() -> new IllegalAccessTokenException("Invalid access token"));

    }

    private Pair<UUID, UUID> parseDecryptedValue(String val) {
        if (!val.contains(DELIMITER))
            throw new IllegalAccessTokenException("Invalid access token");

        String[] uuidStringArray = val.split("\\%s".formatted(DELIMITER));
        try {
            List<UUID> uuidList = Arrays.stream(uuidStringArray)
                    .limit(2)
                    .map(UUID::fromString)
                    .toList();

            return Pair.of(uuidList.get(0), uuidList.get(1));
        } catch (IllegalArgumentException e) {
            throw new IllegalAccessTokenException("Could not evaluate UUID");
        }
    }
}
