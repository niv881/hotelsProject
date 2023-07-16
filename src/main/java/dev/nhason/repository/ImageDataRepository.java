package dev.nhason.repository;

import dev.nhason.entity.ImageData;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ImageDataRepository extends JpaRepository<ImageData,Long> {
    Optional<ImageData> findByName(String name);

    List<ImageData> findImageDataByHotel_NameIgnoreCase(String nane);
    List<ImageData> findAllByHotel_NameIgnoreCase(String name);
}
