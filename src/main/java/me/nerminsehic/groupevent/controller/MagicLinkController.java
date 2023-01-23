package me.nerminsehic.groupevent.controller;

import lombok.RequiredArgsConstructor;
import me.nerminsehic.groupevent.dto.OrganiserDto;
import me.nerminsehic.groupevent.entity.MagicLink;
import me.nerminsehic.groupevent.entity.Organiser;
import me.nerminsehic.groupevent.exception.LinkException;
import me.nerminsehic.groupevent.service.MagicLinkService;
import me.nerminsehic.groupevent.service.OrganiserService;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("api/v1/organisers/{organiserId}/links")
@RequiredArgsConstructor
public class MagicLinkController {

    private final MagicLinkService linkService;
    private final OrganiserService organiserService;
    private final ModelMapper modelMapper;

    @PostMapping
    @CrossOrigin(origins = {"http://localhost:3000", "https://groupevent.co"})
    @ResponseStatus(HttpStatus.CREATED)
    public String createLink(@PathVariable UUID organiserId) {
        Organiser organiser = organiserService.getOrganiserById(organiserId);
        MagicLink link = linkService.create(organiser);
        if (link == null || link.getId() == null)
            throw new LinkException("Failed to create link. Please try again later");

        return "Successfully created a link";
    }

    @CrossOrigin(origins = {"http://localhost:3000", "https://groupevent.co"})
    @PatchMapping("{linkId}")
    @ResponseStatus(HttpStatus.OK)
    public OrganiserDto activateLink(@PathVariable UUID organiserId, @PathVariable UUID linkId) {
        Organiser organiser = organiserService.getOrganiserById(organiserId);
        linkService.activate(organiser, linkId);

        return modelMapper.map(organiser, OrganiserDto.class);
    }
}
