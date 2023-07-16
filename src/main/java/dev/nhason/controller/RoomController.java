package dev.nhason.controller;

import dev.nhason.dto.*;
import dev.nhason.service.AddressService;
import dev.nhason.service.RoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/p1")
public class RoomController {
    private final RoomService roomService;

    @PostMapping("/hotels/{id}/room")
    public ResponseEntity<RoomResponseDto> addToHotelRoom(@RequestBody RoomRequestDto dto,
                                                          @PathVariable(name = "id") long hotel_id,
                                                          UriComponentsBuilder uriBuilder){
        var saved = roomService.addToHotelRoom(dto,hotel_id);
        var uri = uriBuilder
                .path(("/api/p1/hotels/{id}/room_id"))
                .buildAndExpand(hotel_id,saved.getId())
                .toUri();
        return ResponseEntity.created(uri).body(saved);
    }

}
