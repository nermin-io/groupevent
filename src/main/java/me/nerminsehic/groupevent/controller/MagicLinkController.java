package me.nerminsehic.groupevent.controller;

import lombok.RequiredArgsConstructor;
import me.nerminsehic.groupevent.entity.MagicLink;
import me.nerminsehic.groupevent.exception.LinkException;
import me.nerminsehic.groupevent.service.MagicLinkService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("api/v1/organisers/{organiserId}/links")
@RequiredArgsConstructor
public class MagicLinkController {

    private final MagicLinkService linkService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public String createLink(@PathVariable UUID organiserId) {
        MagicLink link = linkService.create(organiserId);
        if(link == null || link.getId() == null)
            throw new LinkException("Failed to create link. Please try again later");

        return "Successfully created a link";
    }

    @PatchMapping("{linkId}")
    @ResponseStatus(HttpStatus.OK)
    public String activateLink(@PathVariable UUID organiserId, @PathVariable UUID linkId) {
        linkService.activate(organiserId, linkId);
        return "Successfully activated link: %s".formatted(linkId);
    }
}
