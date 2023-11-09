package dev.nhason.controller;


import dev.nhason.dto.OrderRoomRequest;
import dev.nhason.dto.OrderRoomResponse;
import dev.nhason.service.OrderRoomService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/order")
public class OrderRoomController {

    private final OrderRoomService orderRoomService;

    @PostMapping()
    public ResponseEntity<OrderRoomResponse> getOrderFromUser(@RequestBody @Valid OrderRoomRequest dto,
                                                              UriComponentsBuilder uriBuilder){
        var saved = orderRoomService.createOrder(dto);
        var uri = uriBuilder
                .path(("/hotels/{id}/address_id"))
                .buildAndExpand(
                        saved.getHotel().getName(),
                        saved.getRoom().getType(),
                        saved.getCheckIn(),
                        saved.getCheckOut())
                .toUri();
        return ResponseEntity.created(uri).body(saved);
    }

    @GetMapping("/order_details")
    public ResponseEntity<List<OrderRoomResponse>> getAllOrdersByUserName(@RequestParam (required = false,defaultValue = "",value = "userName") String userName){
        var orders = orderRoomService.findAllOrderByUserName(userName);
        return ResponseEntity.ok(orders);
    }

}
