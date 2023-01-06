package me.nerminsehic.groupevent.controller;

import lombok.RequiredArgsConstructor;
import me.nerminsehic.groupevent.dto.AddressDto;
import me.nerminsehic.groupevent.entity.Address;
import me.nerminsehic.groupevent.exception.NotFoundException;
import me.nerminsehic.groupevent.service.AddressService;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("api/v1/organisers/{organiserId}/addresses")
@RequiredArgsConstructor
public class AddressController {

    private final AddressService addressService;
    private final ModelMapper modelMapper;

    private AddressDto convertToDto(Address address) {
        return modelMapper.map(address, AddressDto.class);
    }

    private Address convertToEntity(AddressDto addressDto) {
        return modelMapper.map(addressDto, Address.class);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public AddressDto createAddress(@PathVariable UUID organiserId, @RequestBody AddressDto addressDto) {
        Address address = convertToEntity(addressDto);

        return convertToDto(addressService.create(organiserId, address));
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<AddressDto> getAddresses(@PathVariable UUID organiserId) {
        return addressService.findAll(organiserId)
                .stream()
                .map(this::convertToDto)
                .toList();
    }

    @GetMapping("{addressId}")
    @ResponseStatus(HttpStatus.OK)
    public AddressDto getAddressById(@PathVariable UUID organiserId, @PathVariable UUID addressId) {
        Address address = addressService.findById(organiserId, addressId)
                .orElseThrow(() -> new NotFoundException(Address.class, addressId));

        return convertToDto(address);
    }

    @DeleteMapping("{addressId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public String deleteAddressById(@PathVariable UUID organiserId, @PathVariable UUID addressId) {
        addressService.deleteById(organiserId, addressId);
        return "Successfully deleted address %s".formatted(addressId);
    }

    @PutMapping("{addressId}")
    @ResponseStatus(HttpStatus.OK)
    public AddressDto updateAddressById(@PathVariable UUID organiserId, @PathVariable UUID addressId, @RequestBody AddressDto addressDto) {
        Address address = convertToEntity(addressDto);
        return convertToDto(addressService.updateById(organiserId, addressId, address));
    }
}
