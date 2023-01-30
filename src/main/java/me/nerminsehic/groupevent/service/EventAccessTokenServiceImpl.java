package me.nerminsehic.groupevent.service;

import lombok.RequiredArgsConstructor;
import me.nerminsehic.groupevent.entity.Event;
import me.nerminsehic.groupevent.exception.IllegalAccessTokenException;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class EventAccessTokenServiceImpl implements EventAccessTokenService {

    private final EventService eventService;
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

        return eventService.findById(result.getSecond(), result.getFirst())
                .orElseThrow(() -> new IllegalAccessTokenException("Invalid access token"));

    }

    private Pair<UUID, UUID> parseDecryptedValue(String val) {
        if(!val.contains(DELIMITER))
            throw new IllegalAccessTokenException("Invalid access token");

        String[] values = val.split("\\%s".formatted(DELIMITER));

        UUID eventId = UUID.fromString(values[0]);
        UUID organiserId = UUID.fromString(values[1]);

        return Pair.of(eventId, organiserId);
    }
}
