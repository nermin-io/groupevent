package me.nerminsehic.groupevent.controller;

import lombok.RequiredArgsConstructor;
import me.nerminsehic.groupevent.dto.OrganiserDto;
import me.nerminsehic.groupevent.entity.Organiser;
import me.nerminsehic.groupevent.exception.NotFoundException;
import me.nerminsehic.groupevent.service.OrganiserService;
import org.modelmapper.ModelMapper;
import org.springframework.data.util.Pair;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("api/v1/organisers")
@RequiredArgsConstructor
public class OrganiserController {

    private final OrganiserService organiserService;
    private final ModelMapper modelMapper;

    private Organiser convertToEntity(OrganiserDto organiserDto) {
        return modelMapper.map(organiserDto, Organiser.class);
    }

    private OrganiserDto convertToDto(Organiser organiser) {
        return modelMapper.map(organiser, OrganiserDto.class);
    }

    @PostMapping
    @CrossOrigin(origins = {"http://localhost:3000", "https://groupevent.co"})
    @ResponseStatus(HttpStatus.CREATED)
    public OrganiserDto createOrganiser(@Validated @RequestBody OrganiserDto organiserDto) {
        Organiser organiser = convertToEntity(organiserDto);
        return convertToDto(organiserService.create(organiser));
    }

    @CrossOrigin(origins = {"http://localhost:3000", "https://groupevent.co"})
    @PostMapping("find")
    public ResponseEntity<OrganiserDto> findOrCreateOrganiser(@Validated @RequestBody OrganiserDto reqOrganiserDto) {
        Organiser organiser = convertToEntity(reqOrganiserDto);
        Pair<Organiser, Boolean> result = organiserService.findOrCreateOrganiser(organiser);

        OrganiserDto organiserDto = convertToDto(result.getFirst());
        boolean isCreated = result.getSecond();

        if (isCreated)
            return new ResponseEntity<>(organiserDto, HttpStatus.CREATED);

        return new ResponseEntity<>(organiserDto, HttpStatus.OK);
    }

    @CrossOrigin(origins = {"http://localhost:3000", "https://groupevent.co"})
    @PostMapping("login")
    public ResponseEntity<String> organiserAttemptLogin(@Validated @RequestBody OrganiserDto reqOrganiserDto) {
        Organiser organiser = convertToEntity(reqOrganiserDto);
        boolean isCreated = organiserService.attemptLogin(organiser);

        String message = "Please check your email for access token";
        if(isCreated)
            return new ResponseEntity<>(message, HttpStatus.CREATED);

        return new ResponseEntity<>(message, HttpStatus.OK);
    }

    @CrossOrigin(origins = {"http://localhost:3000", "https://groupevent.co"})
    @GetMapping("{id}")
    @ResponseStatus(HttpStatus.OK)
    public OrganiserDto getOrganiserById(@PathVariable UUID id) {
        Organiser organiser = organiserService.findById(id)
                .orElseThrow(() -> new NotFoundException(Organiser.class, id));

        return convertToDto(organiser);
    }

    @CrossOrigin(origins = {"http://localhost:3000", "https://groupevent.co"})
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<OrganiserDto> getOrganisers() {
        return organiserService.findAll()
                .stream()
                .map(this::convertToDto)
                .toList();
    }

    @CrossOrigin(origins = {"http://localhost:3000", "https://groupevent.co"})
    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public String deleteOrganiserById(@PathVariable UUID id) {
        organiserService.deleteById(id);
        return "Successfully deleted organiser %s".formatted(id);
    }

    @CrossOrigin(origins = {"http://localhost:3000", "https://groupevent.co"})
    @PutMapping("{id}")
    @ResponseStatus(HttpStatus.OK)
    public OrganiserDto updateOrganiserById(@PathVariable UUID id, @Validated @RequestBody OrganiserDto organiserDto) {
        Organiser organiser = convertToEntity(organiserDto);
        return convertToDto(organiserService.updateById(id, organiser));
    }
}
