package dev.nhason.service;

import dev.nhason.dto.ImageUploadResponse;
import dev.nhason.entity.ImageData;
import dev.nhason.repository.HotelRepository;
import dev.nhason.repository.ImageDataRepository;
import dev.nhason.utils.ImageUtil;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ImageDataServiceImpl implements ImageDataService {

    private final ImageDataRepository imageDataRepository;
    private final HotelRepository hotelRepository;

    @Override
    public ImageUploadResponse uploadImage(MultipartFile file, String hotelName) throws IOException {
       var hotelE = hotelRepository.findHotelByNameIgnoreCase(hotelName).orElseThrow(
                RuntimeException::new
        );
         imageDataRepository.save(ImageData.builder()
                         .hotel(hotelE)
                 .name(file.getOriginalFilename())
                .type(file.getContentType())
                 .imageData(ImageUtil.compressImage(file.getBytes())).build());

        return new ImageUploadResponse("Image uploaded successfully: ");
    }

    @Transactional
    public ImageData getInfoByImageByName(String name) {
        Optional<ImageData> dbImage = imageDataRepository.findByName(name);

       return ImageData.builder()
                .name(dbImage.get().getName())
                .type(dbImage.get().getType())
                .imageData(ImageUtil.decompressImage(dbImage.get().getImageData())).build();
    }

    @Transactional
    public byte[] getImage(String name) {
        Optional<ImageData> dbImage = imageDataRepository.findByName(name);
        byte[] image = ImageUtil.decompressImage(dbImage.get().getImageData());
        return image;
    }

    @Transactional
    public List<byte[]> getAllHotelImages(String name) {
        List<ImageData> dbImage = imageDataRepository.findAllByHotel_NameIgnoreCase(name);
        List<byte[]> hotelImage = dbImage
                .stream()
                .map(imageData ->
                    ImageUtil.decompressImage(imageData.getImageData())).toList();
        return hotelImage;
    }

}
