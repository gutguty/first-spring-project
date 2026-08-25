package ru.gazprom.server.service;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.gazprom.server.exception.AddressNotFoundException;
import ru.gazprom.server.model.Address;
import ru.gazprom.server.repository.AddressRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AddressService {
    private final AddressRepository addressRepository;

    public List<Address> getAllAddresses() {
        return addressRepository.findAll();
    }

    public Address getAddressById(Long id) {
        return addressRepository.findById(id)
                .orElseThrow(() -> new AddressNotFoundException("Address with id " + id + " not found"));
    }

    public Address createAddress(Address address) {
        if (address.getCity() == null || address.getStreet() == null || address.getCityZone() == null) {
            throw new IllegalArgumentException("Field null");
        }
        return addressRepository.save(address);
    }
}
