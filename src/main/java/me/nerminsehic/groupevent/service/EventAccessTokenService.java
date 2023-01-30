package me.nerminsehic.groupevent.service;

import me.nerminsehic.groupevent.entity.Event;

public interface EventAccessTokenService {
    String createToken(Event event);

    Event verifyToken(String key);
}
