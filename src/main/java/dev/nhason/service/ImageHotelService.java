package dev.nhason.service;

import dev.nhason.dto.ImageUploadResponse;
import dev.nhason.entity.ImageData;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;

public interface ImageHotelService {
    ImageUploadResponse uploadImage(MultipartFile file, String hotelName);
    ImageData getInfoByImageByName(String name);
    byte[] getImage(String name);
    List<byte[]> getAllHotelImages(String name);
}
