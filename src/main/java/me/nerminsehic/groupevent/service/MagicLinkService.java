package me.nerminsehic.groupevent.service;

import me.nerminsehic.groupevent.entity.MagicLink;

import java.util.UUID;

public interface MagicLinkService {

    MagicLink create(UUID organiserId);

    void activate(UUID organiserId, UUID linkId);
}
