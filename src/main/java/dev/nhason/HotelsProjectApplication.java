package dev.nhason;

import dev.nhason.dto.HotelManagementRequestDto;
import dev.nhason.dto.ImageUploadResponse;
import dev.nhason.service.HotelManagement;
import dev.nhason.service.HotelService;
import dev.nhason.service.ImageHotelService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static dev.nhason.utils.FileHotelDataLoader.loadHotelDataFromFile;
import static dev.nhason.utils.FileHotelDataLoader.loadImagesForHotel;

@SpringBootApplication
@RequiredArgsConstructor
public class HotelsProjectApplication{
    private final HotelService hotelService;
    private final HotelManagement hotelManagement;
    private final ImageHotelService imageHotelService;
    public static void main(String[] args) {
        SpringApplication.run(HotelsProjectApplication.class, args);
    }
    @Bean
    public CommandLineRunner initDatabase() {
        return args -> {
            String filePath = "C:\\Users\\Niv\\Documents\\data hotels project\\hotelsData.json";
            List<HotelManagementRequestDto> hotelData = loadHotelDataFromFile(filePath);

            for (HotelManagementRequestDto hotelDto : hotelData) {
                if (!hotelService.hotelExist(hotelDto.getHotel().getName())) {
                    // Hotel with this name does not exist, add it to the database
                    hotelManagement.createHotel(hotelDto);
                    System.out.println("Hotel Name: " + hotelDto.getHotel().getName());
                    // Print other details as needed
                    MultipartFile[] images = loadImagesForHotel(hotelDto.getHotel().getName());
                    String message = "";
                    try{
                        List<String> filesName = new ArrayList<>();
                        Arrays.asList(images)
                                .stream()
                                .forEach(file -> {
                                    imageHotelService.uploadImage(file,hotelDto.getHotel().getName());
                                    filesName.add(file.getOriginalFilename());
                                });
                        message = "Uploaded the files successfully: " + filesName;
                    }catch (Exception e) {
                        message = "Fail to upload files!";
                    }


                } else {
                    System.out.println("Hotel with name " + hotelDto.getHotel().getName() + " already exists in the database. Skipping.");
                }
            }
        };

    }

}
