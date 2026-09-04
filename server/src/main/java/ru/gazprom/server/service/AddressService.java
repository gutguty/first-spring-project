package ru.gazprom.server.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.gazprom.server.exception.AddressNotFoundException;
import ru.gazprom.server.exception.FieldRequiredException;
import ru.gazprom.server.exception.Response;
import ru.gazprom.server.exception.ValidationException;
import ru.gazprom.server.model.Address;
import ru.gazprom.server.repository.AddressRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AddressService {
    private final AddressRepository addressRepository;

    public Response<List<Address>> getAllAddresses() {
        List<Address> addresses = addressRepository.findAll();
        return new Response<>(LocalDateTime.now(), "getAllAddresses", true, addresses, List.of());
    }

    public Response<Address> getAddressById(Long id) {
        return addressRepository.findById(id)
                .map(address -> new Response<>(LocalDateTime.now(), "getAddressById", true, address, List.of()))
                .orElseGet(() -> new Response<>(LocalDateTime.now(), "getAddressById", false, null,
                        List.of(new AddressNotFoundException("Address with id " + id + " not found"))));
    }

    public Response<Address> createAddress(Address address) {
        List<ValidationException> errors = new ArrayList<>();
        if (address.getCity() == null) {
            errors.add(new FieldRequiredException("city"));
        }
        if (address.getStreet() == null) {
            errors.add(new FieldRequiredException("street"));
        }
        if (address.getCityZone() == null) {
            errors.add(new FieldRequiredException("cityZone"));
        }
        if (!errors.isEmpty()) {
            return new Response<>(LocalDateTime.now(), "createAddress", false, null, errors);
        }
        Address saved = addressRepository.save(address);
        return new Response<>(LocalDateTime.now(), "createAddress", true, saved, List.of());
    }
}