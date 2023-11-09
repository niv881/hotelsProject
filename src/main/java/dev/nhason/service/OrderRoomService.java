package dev.nhason.service;

import dev.nhason.dto.OrderRoomRequest;
import dev.nhason.dto.OrderRoomResponse;

import java.util.List;

public interface OrderRoomService {
    OrderRoomResponse createOrder(OrderRoomRequest dto);
    List<OrderRoomResponse> findAllOrderByUserName(String userName);
}
