package ru.gazprom.server.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.gazprom.server.model.Address;
import ru.gazprom.server.service.AddressService;

import java.util.List;


@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class AddressController {
    private final AddressService addressService;

    @GetMapping("/addresses")
    public List<Address> getAllAddresses() {
        return addressService.getAllAddresses();
    }

    @GetMapping("/addresses/{id}")
    public Address getAddressById(@PathVariable Long id) {
        return addressService.getAddressById(id);
    }

    @PostMapping("/addresses")
    public Address createAddress(@RequestBody Address address) {
        return addressService.createAddress(address);
    }
}