package dev.nhason.controller;

import dev.nhason.dto.ImageUploadResponse;
import dev.nhason.entity.ImageData;
import dev.nhason.service.ImageDataService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("api/p1/image")
@RequiredArgsConstructor
public class ImageDataController {

    private final ImageDataService imageDataService;

    @PostMapping
    public ResponseEntity<?> uploadImage(@RequestParam(value = "hotel_name") String hotelName ,
                                         @RequestParam(value = "image") MultipartFile file) throws IOException {
        ImageUploadResponse response = imageDataService.uploadImage(file,hotelName);

        return ResponseEntity.status(HttpStatus.OK)
                .body(response);
    }

    @GetMapping("/info/{name}")
    public ResponseEntity<?>  getImageInfoByName(@PathVariable("name") String name){
        ImageData image = imageDataService.getInfoByImageByName(name);

        return ResponseEntity.status(HttpStatus.OK)
                .body(image);
    }

//    @GetMapping("/{name}")
//    public ResponseEntity<?>  getImageByName(@PathVariable("name") String name){
//        byte[] image = imageDataService.getImage(name);
//
//        return ResponseEntity.status(HttpStatus.OK)
//                .contentType(MediaType.valueOf("image/png"))
//                .body(image);
//    }

    @GetMapping("/{name}")
    public ResponseEntity<?>  getAllHotelImages(@PathVariable("name") String name){
        List<byte[]> image = imageDataService.getAllHotelImages(name);

        return ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaType.valueOf("image/png"))
                .body(image.stream().findAny().orElseThrow());
    }
}
