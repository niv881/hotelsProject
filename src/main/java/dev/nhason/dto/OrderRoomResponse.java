package dev.nhason.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderRoomResponse {
    private LocalDate checkIn;
    private LocalDate checkOut;
    private HotelResponseDto hotel;
    private RoomResponseDto room;
}
