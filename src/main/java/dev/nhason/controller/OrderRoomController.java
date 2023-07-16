package dev.nhason.controller;


import dev.nhason.dto.OrderRoomRequest;
import dev.nhason.dto.OrderRoomResponse;
import dev.nhason.service.OrderRoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/p1/order")
public class OrderRoomController {

    private final OrderRoomService orderRoomService;

    @PostMapping()
    public ResponseEntity<OrderRoomResponse> giveHotelAddress(@RequestBody OrderRoomRequest dto,
                                                              UriComponentsBuilder uriBuilder){
        var saved = orderRoomService.createOrder(dto);
        var uri = uriBuilder
                .path(("/api/p1/hotels/{id}/address_id"))
                .buildAndExpand(
                        saved.getHotel().getName(),
                        saved.getRoom().getType(),
                        saved.getCheckIn(),
                        saved.getCheckOut())
                .toUri();
        return ResponseEntity.created(uri).body(saved);
    }

}
