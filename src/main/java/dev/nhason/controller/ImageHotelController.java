package dev.nhason.controller;

import dev.nhason.dto.ImageUploadResponse;
import dev.nhason.entity.ImageData;
import dev.nhason.service.ImageHotelService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/hotel_Image")
@RequiredArgsConstructor
public class ImageHotelController {

    private  final ImageHotelService imageHotelService;

    @PostMapping
    public ResponseEntity<ImageUploadResponse> uploadImage(@RequestParam(value = "hotel_name") String hotelName
            , @RequestParam(value = "image") MultipartFile[] files
    , UriComponentsBuilder uriBuilder)  {

        String message = "";
        var uri = uriBuilder.path("/image/upload").buildAndExpand(files).toUri();

        try{
            List<String> filesName = new ArrayList<>();
            Arrays.asList(files)
                    .stream()
                    .forEach(file -> {
                        imageHotelService.uploadImage(file,hotelName);
                        filesName.add(file.getOriginalFilename());
                    });
            message = "Uploaded the files successfully: " + filesName;
            return ResponseEntity.created(uri).body(new ImageUploadResponse(message));
        }catch (Exception e) {
            message = "Fail to upload files!";
            return ResponseEntity.created(uri).body(new ImageUploadResponse(message + e.getMessage()));
        }
    }

    @GetMapping("/info/{name}")
    public ResponseEntity<?>  getImageInfoByName(@PathVariable("name") String name){
        ImageData image = imageHotelService.getInfoByImageByName(name);

        return ResponseEntity.status(HttpStatus.OK)
                .body(image);
    }

    @GetMapping("/{name}")
    public ResponseEntity<byte[]> getImageByName(@PathVariable("name") String name){
        byte[] image = imageHotelService.getImage(name);

        return ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaType.valueOf("image/png"))
                .body(image);
    }

//    @GetMapping("/{name}")
//    public ResponseEntity<?>  getAllHotelImages(@PathVariable("name") String name){
//        List<byte[]> image = imageHotelService.getAllHotelImages(name);
//
//        return ResponseEntity.status(HttpStatus.OK)
//                .contentType(MediaType.valueOf("image/png"))
//                .body(image.stream().findAny().orElseThrow());
//    }

}