package dev.nhason.controller;

import dev.nhason.dto.HotelResponseDto;
import dev.nhason.dto.HotelsRequestDto;
import dev.nhason.service.HotelService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping("api/p1/hotels")
@RequiredArgsConstructor
public class HotelController {
    private final HotelService hotelService;
    @PostMapping
    public ResponseEntity<HotelResponseDto> createHotel(@Valid @RequestBody HotelsRequestDto dto
    , UriComponentsBuilder uriBuilder){
        var saved = hotelService.createHotel(dto);
        var uri = uriBuilder.path("api/p1/hotels").buildAndExpand(saved.getId()).toUri();
        return ResponseEntity.created(uri).body(saved);
    }
}
