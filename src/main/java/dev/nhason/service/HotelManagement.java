package dev.nhason.service;

import dev.nhason.dto.*;

public interface HotelManagement {
    HotelManagementDto getHotelDetailsByHotelName(String hotelName);

    HotelsManagementResponseDto getHotelDetailsByHotelAddress(String hotelCountry, String hotelCity);

    Boolean createHotel(HotelManagementRequestDto dto);

    HotelResponseDto updateHotelDetails(HotelsRequestDto dto);

    RoomResponseDto updateHotelRoom(RoomRequestDto dto,String hotelName);

}
