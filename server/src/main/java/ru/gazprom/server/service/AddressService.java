package ru.gazprom.server.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.gazprom.server.exception.AddressNotFoundError;
import ru.gazprom.server.exception.FieldRequiredError;
import ru.gazprom.server.exception.Response;
import ru.gazprom.server.exception.ValidationError;
import ru.gazprom.server.model.Address;
import ru.gazprom.server.repository.AddressRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static ru.gazprom.server.utils.ResponseUtils.responseError;
import static ru.gazprom.server.utils.ResponseUtils.responseSuccess;

@Service
@RequiredArgsConstructor
public class AddressService {
    private final AddressRepository addressRepository;

    public Response<List<Address>> getAllAddresses() {
        List<Address> addresses = addressRepository.findAll();
        return responseSuccess("getAllAddresses", addresses);
    }

    public Response<Address> getAddressById(Long id) {
        return addressRepository.findById(id)
                .map(address -> responseSuccess("getAddressById", address))
                .orElseGet(() -> responseError("getAddressById", new AddressNotFoundError("Address with id " + id + " not found")));
    }

    public Response<Address> createAddress(Address address) {
        List<ValidationError> errors = new ArrayList<>();
        if (address.getCity() == null) {
            errors.add(new FieldRequiredError("city"));
        }
        if (address.getStreet() == null) {
            errors.add(new FieldRequiredError("street"));
        }
        if (address.getCityZone() == null) {
            errors.add(new FieldRequiredError("cityZone"));
        }
        if (!errors.isEmpty()) {
            return responseError("createAddress", errors);
        }
        Address saved = addressRepository.save(address);
        return responseSuccess("createAddress", saved);
    }

    public Optional<Address> findAddressById(Long addressId) {
        return addressRepository.findById(addressId);
    }
}