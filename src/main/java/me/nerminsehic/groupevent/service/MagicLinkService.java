package me.nerminsehic.groupevent.service;

import me.nerminsehic.groupevent.entity.MagicLink;
import me.nerminsehic.groupevent.entity.Organiser;

import java.util.UUID;

public interface MagicLinkService {

    MagicLink create(Organiser organiser);

    void activate(Organiser organiser, UUID linkId);
}
