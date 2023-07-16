package dev.nhason.service;

import dev.nhason.dto.HotelResponseDto;
import dev.nhason.dto.HotelsRequestDto;
import dev.nhason.entity.Hotel;
import dev.nhason.repository.HotelRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class HotelServiceImpl implements HotelService {

    private final HotelRepository hotelRepository;
    private final ModelMapper modelMapper;
    @Override
    public HotelResponseDto createHotel(HotelsRequestDto hotelsRequestDto) {
        var entity = modelMapper.map(hotelsRequestDto, Hotel.class);
        var saved = hotelRepository.save(entity);
        return modelMapper.map(saved, HotelResponseDto.class);
    }
}
