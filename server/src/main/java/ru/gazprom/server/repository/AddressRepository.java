package ru.gazprom.server.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.gazprom.server.model.Address;

@Repository
public interface AddressRepository extends JpaRepository<Address, Long> {
}
