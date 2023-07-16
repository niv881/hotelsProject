package dev.nhason.service;

import dev.nhason.dto.HotelResponseDto;
import dev.nhason.dto.HotelsRequestDto;

public interface HotelService {
    HotelResponseDto createHotel(HotelsRequestDto hotelsRequestDto);
}
