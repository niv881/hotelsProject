package dev.nhason.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.sql.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderRoomResponse {
    private Date checkIn;
    private Date checkOut;
    private int roomCapacity;
    private HotelResponseDto hotel;
    private RoomResponseDto room;
    private UserResponseDto user;
    private String orderNumber;
}
