package dev.nhason.service;

import dev.nhason.dto.AddressRequestDto;
import dev.nhason.dto.AddressResponseDto;
import dev.nhason.dto.HotelWithAddress;
import dev.nhason.entity.Address;
import dev.nhason.entity.Hotel;
import dev.nhason.repository.AddressRepository;
import dev.nhason.repository.HotelRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AddressServiceImpl implements AddressService {
    private final AddressRepository addressRepository;
    private final HotelRepository hotelRepository;
    private final ModelMapper modelMapper;
    @Override
    public AddressResponseDto giveAddressToHotel(AddressRequestDto addressRequestDto,long hotel_id) {
        Hotel hotel = hotelRepository.findById(hotel_id).orElseThrow(
                RuntimeException::new
        );
        var entity = modelMapper.map(addressRequestDto, Address.class);
        entity.setHotel(hotel);
        var saved = addressRepository.save(entity);
        return modelMapper.map(saved,AddressResponseDto.class);
    }

    @Override
    public HotelWithAddress getAddressByHotelName(String hotelName) {
        var address = addressRepository.findAddressByHotel_Name(hotelName);
        AddressResponseDto ard = modelMapper.map(address,AddressResponseDto.class);

        var hotel = address.getHotel();
        return HotelWithAddress.builder()
                .name(hotel.getName())
                .address(ard)
                .id(hotel.getId())
                .about(hotel.getAbout().toString())
                .email(hotel.getEmail())
                .phoneNumber(hotel.getPhoneNumber())
                .build();
    }
}
