package dev.nhason.controller;

import dev.nhason.dto.AddressRequestDto;
import dev.nhason.dto.AddressResponseDto;
import dev.nhason.dto.HotelWithAddress;
import dev.nhason.service.AddressService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/p1")
public class AddressController {
    private final AddressService addressService;

    @PostMapping("/hotels/{id}/address")
    public ResponseEntity<AddressResponseDto> giveHotelAddress(@RequestBody AddressRequestDto dto,
                                                               @PathVariable(name = "id") long hotel_id,
                                                               UriComponentsBuilder uriBuilder){
        var saved = addressService.giveAddressToHotel(dto,hotel_id);
        var uri = uriBuilder
                .path(("/api/p1/hotels/{id}/address_id"))
                .buildAndExpand(hotel_id,saved.getId())
                .toUri();
        return ResponseEntity.created(uri).body(saved);
    }

    @GetMapping("/hotels/address")
    public ResponseEntity<HotelWithAddress> getHotelWithAddress(@RequestParam(value = "hotelName")String hotelName){
        return ResponseEntity.ok(addressService.getAddressByHotelName(hotelName));
    }

}
