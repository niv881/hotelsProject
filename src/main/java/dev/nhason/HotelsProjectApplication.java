package dev.nhason;

import dev.nhason.dto.HotelManagementRequestDto;
import dev.nhason.dto.SignUpRequestDto;
import dev.nhason.error.BadRequestException;
import dev.nhason.service.HotelManagement;
import dev.nhason.service.HotelService;
import dev.nhason.service.ImageHotelService;
import dev.nhason.service.UserDetailsServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
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
    private final UserDetailsServiceImpl userDetailsServiceImpl;

    public static void main(String[] args) {
        SpringApplication.run(HotelsProjectApplication.class, args);
    }
    @Bean
    public CommandLineRunner initDatabase() {
        return args -> {
            if (!userDetailsServiceImpl.checkIfUserExist("defaultUser") ||
                    !userDetailsServiceImpl.checkIfUserExist("defaultManager") ||
            !userDetailsServiceImpl.checkIfUserExist("defaultAdmin")) {
                SignUpRequestDto defaultUserDto = new SignUpRequestDto();
                defaultUserDto.setUsername("defaultUser");
                defaultUserDto.setEmail("defaultUser@example.com");
                defaultUserDto.setPassword("12345678Aa");

                userDetailsServiceImpl.signUp(defaultUserDto);

                SignUpRequestDto defaultManagerDto = new SignUpRequestDto();
                defaultManagerDto.setUsername("defaultManager");
                defaultManagerDto.setEmail("defaultManager@example.com");
                defaultManagerDto.setPassword("12345678Aa");

                userDetailsServiceImpl.signUpManager(defaultManagerDto);

                SignUpRequestDto defaultAdminDto = new SignUpRequestDto();
                defaultAdminDto.setUsername("defaultAdmin");
                defaultAdminDto.setEmail("defaultAdmin@example.com");
                defaultAdminDto.setPassword("12345678Aa");

                userDetailsServiceImpl.signUpAdmin(defaultAdminDto);

                System.out.println(" All the Default user created successfully.");
            } else {
                System.out.println("Default user already exists. Skipping creation.");
            }


            String filePath = "your path to the data hotels project file";
            String imagePath = "your path to the data hotels project assets file";
            List<HotelManagementRequestDto> hotelData = loadHotelDataFromFile(filePath);

            for (HotelManagementRequestDto hotelDto : hotelData) {
                if (!hotelService.hotelExist(hotelDto.getHotel().getName())) {
                    hotelManagement.createHotel(hotelDto);
                    MultipartFile[] images = loadImagesForHotel(hotelDto.getHotel().getName(),filePath,imagePath);

                    try{
                        List<String> filesName = new ArrayList<>();
                        Arrays.asList(images)
                                .stream()
                                .forEach(file -> {
                                    imageHotelService.uploadImage(file,hotelDto.getHotel().getName());
                                    filesName.add(file.getOriginalFilename());
                                });
                    }catch (Exception e) {
                        new BadRequestException("Bad Requests : "+e);
                    }


                } else {
                    System.out.println("Hotel with name " + hotelDto.getHotel().getName() + " already exists in the database. Skipping.");
                }
            }
        };

    }

}
