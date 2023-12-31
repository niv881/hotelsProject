package dev.nhason.utils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.nhason.dto.AddressRequestDto;
import dev.nhason.dto.HotelManagementRequestDto;
import dev.nhason.dto.HotelsRequestDto;
import dev.nhason.dto.RoomRequestDto;
import dev.nhason.service.HotelManagement;
import lombok.RequiredArgsConstructor;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class FileHotelDataLoader {

    public static List<HotelManagementRequestDto> loadHotelDataFromFile(String filePath) {
        List<HotelManagementRequestDto> hotelDtos = new ArrayList<>();

        try {
            File file = new File(filePath);
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode hotelData = objectMapper.readTree(file);

            // Process the JSON data and create DTOs
            for (JsonNode hotelNode : hotelData) {
                HotelManagementRequestDto hotelDto = createHotelDtoFromJson(hotelNode);
                hotelDtos.add(hotelDto);
            }

        } catch (IOException e) {
            e.printStackTrace();
            // Handle the exception (e.g., log an error, throw a custom exception)
        }

        return hotelDtos;
    }

    private static HotelManagementRequestDto createHotelDtoFromJson(JsonNode hotelNode) {
        JsonNode hotelDetails = hotelNode.path("hotel");
        JsonNode addressDetails = hotelNode.path("address");
        JsonNode roomsArray = hotelNode.path("rooms");

        List<RoomRequestDto> roomDtos = new ArrayList<>();

        for (JsonNode roomNode : roomsArray) {
            RoomRequestDto roomDto = RoomRequestDto.builder()
                    .type(roomNode.path("type").asText())
                    .price(roomNode.path("price").asDouble())
                    .capacity(roomNode.path("capacity").asInt())
                    .build();
            roomDtos.add(roomDto);
        }

        return HotelManagementRequestDto.builder()
                .hotel(HotelsRequestDto.builder()
                        .name(hotelDetails.path("name").asText())
                        .about(hotelDetails.path("about").asText())
                        .email(hotelDetails.path("email").asText())
                        .phoneNumber(hotelDetails.path("phoneNumber").asText())
                        .build())
                .address(AddressRequestDto.builder()
                        .country(addressDetails.path("country").asText())
                        .city(addressDetails.path("city").asText())
                        .street(addressDetails.path("street").asText())
                        .streetNumber(addressDetails.path("streetNumber").asText())
                        .build())
                .rooms(roomDtos)
                .build();
    }


    public static MultipartFile[] loadImagesForHotel(String hotelName,String filePath,String imagePath) {
        try {
            File file = new File(filePath);
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode hotelData = objectMapper.readTree(file);

            // Find the hotel node with the specified hotelName
            JsonNode targetHotelNode = null;
            for (JsonNode hotelNode : hotelData) {
                String currentHotelName = hotelNode.path("hotel").path("name").asText();
                if (hotelName.equals(currentHotelName)) {
                    targetHotelNode = hotelNode;
                    break;
                }
            }

            if (targetHotelNode != null) {
                // Process the JSON data for the target hotel and create MultipartFile objects
                List<MultipartFile> imageFiles = new ArrayList<>();
                for (JsonNode imageNode : targetHotelNode.path("images")) {
                    String fileName = imageNode.path("fileName").asText();
                    String caption = imageNode.path("caption").asText();
                    byte[] imageDataBytes = Files.readAllBytes(Paths.get(imagePath,caption,fileName));
                    MultipartFile multipartFile = new MockMultipartFile(fileName, fileName, "image/jpeg", imageDataBytes);
                    imageFiles.add(multipartFile);
                }

                return imageFiles.toArray(new MultipartFile[0]);
            } else {
                // Handle the case where the specified hotelName is not found
                System.err.println("Hotel not found: " + hotelName);
                return null;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

}
