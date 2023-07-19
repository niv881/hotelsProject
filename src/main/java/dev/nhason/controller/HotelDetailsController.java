package dev.nhason.controller;

import dev.nhason.dto.*;
import dev.nhason.service.HotelManagement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping("/hotels_management")
@RequiredArgsConstructor
public class HotelDetailsController {
    private final HotelManagement hotelManagement;

    @GetMapping
    public ResponseEntity<AllHotelsManagementResponseDto> getDetailsByHotelAddress(
            @RequestParam (required = false,defaultValue = "", value = "hotel_country") String hotelCountry,
             @RequestParam (required = false,defaultValue = "",value = "hotel_city") String hotelCity){
        return ResponseEntity.ok(hotelManagement.getHotelDetailsByHotelAddress(hotelCountry,hotelCity));
    }

    //TODO : extract HotelManagement for Validation : atk HotelRequest, AddressRequest...
    @PostMapping("/create_new_hotel")
    public ResponseEntity<HotelManagementResponseDto> addHotelToDataBase(
            @RequestBody @Valid HotelManagementRequestDto dto
    , UriComponentsBuilder uriBuilder){
        var saved = hotelManagement.createHotel(dto);
        var uri = uriBuilder.path("/api/v1/create").buildAndExpand(saved).toUri();
        return ResponseEntity.created(uri).body(saved);
    }

    @PutMapping("/update/hotel_details")
    public ResponseEntity<HotelResponseDto> updateHotelByHotelName(@RequestBody HotelsRequestDto dto
            , UriComponentsBuilder uriBuilder){
        var saved = hotelManagement.updateHotelDetails(dto);
        var uri = uriBuilder.path("/api/v1/update").buildAndExpand(saved).toUri();
        return ResponseEntity.created(uri).body(saved);
    }

    @PutMapping("/update/hotel_rooms")
    public ResponseEntity<RoomResponseDto> updateHotelByHotelName(
            @RequestBody RoomRequestDto dto,
            @RequestParam (value = "hotel_name") String hotelName,
            UriComponentsBuilder uriBuilder){
        var saved = hotelManagement.updateHotelRoom(dto,hotelName);
        var uri = uriBuilder.path("/api/v1/update").buildAndExpand(saved).toUri();
        return ResponseEntity.created(uri).body(saved);
    }



}
