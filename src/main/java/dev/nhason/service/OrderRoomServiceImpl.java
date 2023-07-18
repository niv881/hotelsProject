package dev.nhason.service;

import dev.nhason.dto.HotelResponseDto;
import dev.nhason.dto.OrderRoomRequest;
import dev.nhason.dto.OrderRoomResponse;
import dev.nhason.dto.RoomResponseDto;
import dev.nhason.entity.Hotel;
import dev.nhason.entity.OrderRoom;
import dev.nhason.entity.Room;
import dev.nhason.error.BadRequestException;
import dev.nhason.error.NotFoundException;
import dev.nhason.repository.HotelRepository;
import dev.nhason.repository.OrderRoomRepository;
import dev.nhason.repository.RoomRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

@Service
@RequiredArgsConstructor
public class OrderRoomServiceImpl implements OrderRoomService {
    private final OrderRoomRepository orderRoomRepository;
    private final RoomRepository roomRepository;
    private final HotelRepository hotelRepository;
    private final ModelMapper modelMapper;

    @Override
    public OrderRoomResponse createOrder(OrderRoomRequest dto) {
        var hotelE = hotelRepository.findHotelByNameIgnoreCase(dto.getHotelName()).orElseThrow(
                () -> new BadRequestException(dto.getHotelName())
        );
        var roomsE = roomRepository.
                findRoomsByHotel_Name(dto.getHotelName()).orElseThrow(
                () -> new BadRequestException(dto.getHotelName(), "No rooms for this hotel!")
        );
        var roomC = roomsE.
                stream()
                .filter(room -> dto.getRoomType().equals(room.getType()))
                .findAny()
                .orElseThrow(
                        () -> new NotFoundException(dto.getRoomType(), "this type of room doesn't found"));

        String CheckIn = dto.getCheckIn().toString();
        String dateFormat = "yyyy-MM-dd";

        if(isDatePast(CheckIn,dateFormat)){
            throw new BadRequestException(dto.getCheckIn().toString(), "The CheckIn Date is passed .. ");
        }




        if (roomC.getCapacity() > 0) {
            makeOrder(dto, hotelE, roomC);
            updateCapacity( dto, roomC);

            roomRepository.save(roomC);

            var hotel = modelMapper.map(hotelE, HotelResponseDto.class);
            var room = modelMapper.map(roomC, RoomResponseDto.class);


            return OrderRoomResponse.builder()
                    .checkIn(dto.getCheckIn())
                    .checkOut(dto.getCheckOut())
                    .hotel(hotel)
                    .room(room)
                    .roomCapacity(dto.getRoomCapacity())
                    .build();
        } else {
            throw new BadRequestException(dto.getRoomType(),"sorry this room type is no available. " +
                    "we have another room that waiting for you!");
        }
    }

    private boolean isDatePast(String date,String dataFormat){
        LocalDate localDate = LocalDate.now(ZoneId.systemDefault());
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern(dataFormat);
        LocalDate inputDate = LocalDate.parse(date,dtf);

        return inputDate.isBefore(localDate);
    }


    //helper Methods :
    private void updateCapacity(OrderRoomRequest dto, Room roomC) {
            roomC.setCapacity(roomC.getCapacity() - dto.getRoomCapacity());
            roomRepository.save(roomC);
    }

    private void makeOrder(OrderRoomRequest dto, Hotel hotelE, Room roomC) {
        var orderE = OrderRoom.builder()
                .checkIn(dto.getCheckIn())
                .checkOut(dto.getCheckOut())
                .hotel(hotelE)
                .room(roomC)
                .roomCapacity(dto.getRoomCapacity())
                .build();
        orderRoomRepository.save(orderE);

        var ordersFromHotel = hotelE.getOrder();
        ordersFromHotel.add(orderE);
        hotelE.setOrder(ordersFromHotel);
        hotelRepository.save(hotelE);

        var ordersFromRoom = roomC.getOrder();
        ordersFromRoom.add(orderE);
        roomC.setOrder(ordersFromRoom);
        roomRepository.save(roomC);
    }


}
