package dev.nhason.service;

import dev.nhason.dto.HotelResponseDto;
import dev.nhason.dto.OrderRoomRequest;
import dev.nhason.dto.OrderRoomResponse;
import dev.nhason.dto.RoomResponseDto;
import dev.nhason.entity.OrderRoom;
import dev.nhason.repository.HotelRepository;
import dev.nhason.repository.OrderRoomRepository;
import dev.nhason.repository.RoomRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Service
@RequiredArgsConstructor
public class OrderRoomServiceImpl implements OrderRoomService {
    private final OrderRoomRepository orderRoomRepository;
    private final RoomRepository roomRepository;
    private final HotelRepository hotelRepository;
    private final ModelMapper modelMapper;

    @Override
    public OrderRoomResponse createOrder(OrderRoomRequest dto){
        var hotelE = hotelRepository.findHotelByNameIgnoreCase(dto.getHotelName()).orElseThrow(
                //TODO : message if dont find the hotel
                RuntimeException::new
        );
        var roomsE = roomRepository.findRoomsByHotel_Id(hotelE.getId()).orElseThrow(
                //TODO : message if dont find the room in the hotel
                RuntimeException::new
        );
        var roomC = roomsE.
                stream()
                .filter(room -> dto.getRoomType().equals(room.getType()))
                .findAny()
                .orElseThrow(
                //TODO : message if dont find the room
                        RuntimeException::new);

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            var checkIn = dto.getCheckIn();
            LocalDate localDateCheckIn = LocalDate.parse(checkIn,formatter);
            var checkOut = dto.getCheckOut();
            LocalDate localDateCheckOut = LocalDate.parse(checkOut,formatter);




            var orderE = OrderRoom.builder()
                    .checkIn(localDateCheckIn)
                    .checkOut(localDateCheckOut)
                    .hotel(hotelE)
                    .room(roomC)
                    .build();
            orderRoomRepository.save(orderE);

            roomC.setCapacity(roomC.getCapacity()-dto.getRoomCapacity());
            roomRepository.save(roomC);

            var hotel = modelMapper.map(hotelE, HotelResponseDto.class);
            var room = modelMapper.map(roomC, RoomResponseDto.class);



            return OrderRoomResponse.builder()
                    .checkIn(localDateCheckIn)
                    .checkOut(localDateCheckOut)
                    .hotel(hotel)
                    .room(room)
                    .build();
    }
}
