package dev.nhason.repository;


import dev.nhason.entity.Hotel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;


public interface HotelRepository extends JpaRepository<Hotel,Long> {
    List<Hotel> findAllByAddress_CountryOrAddress_City(String country,String city);
    Optional<Hotel>findHotelByNameIgnoreCase(String name);

}
