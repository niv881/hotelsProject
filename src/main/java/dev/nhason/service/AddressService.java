package dev.nhason.service;

import dev.nhason.dto.AddressRequestDto;
import dev.nhason.dto.AddressResponseDto;
import dev.nhason.dto.HotelWithAddress;

public interface AddressService {
    AddressResponseDto giveAddressToHotel(AddressRequestDto addressRequestDto, long hotel_id);
    HotelWithAddress getAddressByHotelName(String hotelName);
}
