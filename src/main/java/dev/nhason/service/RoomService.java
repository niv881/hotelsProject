package dev.nhason.service;

import dev.nhason.dto.*;

public interface RoomService {
    RoomResponseDto addToHotelRoom(RoomRequestDto roomRequestDto, long hotel_id);

}
