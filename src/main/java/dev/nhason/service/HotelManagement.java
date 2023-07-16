package dev.nhason.service;

import dev.nhason.dto.*;

public interface HotelManagement {
    HotelManagementResponseDto getHotelDetailsByHotelName(String hotelName);

    AllHotelsManagementResponseDto getHotelDetailsByHotelAddress(String hotelCountry, String hotelCity);

    HotelManagementResponseDto createHotel(HotelManagementRequestDto dto);

    HotelResponseDto updateHotelDetails(HotelsRequestDto dto);

    RoomResponseDto updateHotelRoom(RoomRequestDto dto,String hotelName);

}
