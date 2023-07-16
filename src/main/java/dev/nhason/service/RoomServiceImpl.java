package dev.nhason.service;

import dev.nhason.dto.RoomRequestDto;
import dev.nhason.dto.RoomResponseDto;
import dev.nhason.entity.Room;
import dev.nhason.repository.HotelRepository;
import dev.nhason.repository.RoomRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RoomServiceImpl implements RoomService {
    private final RoomRepository roomRepository;
    private final HotelRepository hotelRepository;
    private final ModelMapper modelMapper;
    @Override
    public RoomResponseDto addToHotelRoom(RoomRequestDto roomRequestDto, long hotel_id) {
        var hotel = hotelRepository.findById(hotel_id).orElseThrow(
                RuntimeException::new
        );
        var entity = modelMapper.map(roomRequestDto, Room.class);
        entity.setHotel(hotel);
        var saved = roomRepository.save(entity);

        return modelMapper.map(saved,RoomResponseDto.class);
    }
}
