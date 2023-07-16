package dev.nhason.service;

import dev.nhason.dto.OrderRoomRequest;
import dev.nhason.dto.OrderRoomResponse;

public interface OrderRoomService {
    OrderRoomResponse createOrder(OrderRoomRequest dto);
}
