package me.nerminsehic.groupevent.service;

import lombok.RequiredArgsConstructor;
import me.nerminsehic.groupevent.entity.LinkStatus;
import me.nerminsehic.groupevent.entity.MagicLink;
import me.nerminsehic.groupevent.entity.Organiser;
import me.nerminsehic.groupevent.exception.LinkException;
import me.nerminsehic.groupevent.exception.NotFoundException;
import me.nerminsehic.groupevent.repository.MagicLinks;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MagicLinkServiceImpl implements MagicLinkService {

    private final MagicLinks magicLinks;
    private final OrganiserService organiserService;
    private final MailService mailService;

    @Override
    public MagicLink create(UUID organiserId) {
        Organiser organiser = organiserService.getOrganiserById(organiserId);
        MagicLink link = new MagicLink(organiser);

        MagicLink persistedLink = magicLinks.save(link);
        mailService.sendLinkToOrganiser(organiser, persistedLink);

        return persistedLink;
    }

    @Override
    public void activate(UUID organiserId, UUID linkId) {
        Organiser organiser = organiserService.getOrganiserById(organiserId);
        MagicLink link = magicLinks.findByIdAndOrganiser(linkId, organiser)
                .orElseThrow(() -> new NotFoundException(MagicLink.class, linkId));

        Instant now = Instant.now();
        if(now.isAfter(link.getExpiresAt())) // check if the link has expired
            throw new LinkException("The link expired at %s. Current Time: %s".formatted(link.getExpiresAt(), now));

        if(link.getStatus() == LinkStatus.CLOSED) // check if the link has already been activated
            throw new LinkException("The link has already been activated");

        link.setStatus(LinkStatus.CLOSED);
        magicLinks.save(link);
    }
}
