package dev.nhason.repository;

import dev.nhason.entity.Address;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AddressRepository extends JpaRepository<Address,Long> {
        Address findAddressByHotel_Name(String name);
        Optional<Address> findAddressByCity(String city);
        List<Address> findAll();

}
