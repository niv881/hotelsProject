package dev.nhason.controller;

import dev.nhason.dto.ImageUploadResponse;
import dev.nhason.entity.ImageData;
import dev.nhason.service.ImageHotelService;
import dev.nhason.utils.ImageInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/hotel_Image")
@RequiredArgsConstructor
public class ImageHotelController {

    private  final ImageHotelService imageHotelService;

    @PreAuthorize("hasRole('MANAGER')")
    @PostMapping("/upload_image")
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

}
