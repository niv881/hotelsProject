package dev.nhason.service;

import dev.nhason.dto.AddressRequestDto;
import dev.nhason.dto.AddressResponseDto;
import dev.nhason.dto.HotelWithAddress;

import java.util.List;

public interface AddressService {
    AddressResponseDto giveAddressToHotel(AddressRequestDto addressRequestDto, long hotel_id);
    HotelWithAddress getAddressByHotelName(String hotelName);
    List<AddressResponseDto> getAllAddress();
}
