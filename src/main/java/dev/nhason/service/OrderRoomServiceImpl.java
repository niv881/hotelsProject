package dev.nhason.service;

import dev.nhason.dto.*;
import dev.nhason.entity.Hotel;
import dev.nhason.entity.OrderRoom;
import dev.nhason.entity.Room;
import dev.nhason.entity.User;
import dev.nhason.error.BadRequestException;
import dev.nhason.error.NotFoundException;
import dev.nhason.repository.HotelRepository;
import dev.nhason.repository.OrderRoomRepository;
import dev.nhason.repository.RoomRepository;
import dev.nhason.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.Conditions;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderRoomServiceImpl implements OrderRoomService {
    private final OrderRoomRepository orderRoomRepository;
    private final RoomRepository roomRepository;
    private final HotelRepository hotelRepository;
    private final UserRepository userRepository;
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

        var user = userRepository.findByUsernameIgnoreCase(dto.getUserName()).orElseThrow(
                () -> new NotFoundException("Not found this user ! ")
        );

        String CheckIn = dto.getCheckIn().toString();
        String dateFormat = "yyyy-MM-dd";

        checkExpireOrders(roomC.getType(),dateFormat);

        if(isDatePast(CheckIn,dateFormat)){
            throw new BadRequestException(dto.getCheckIn().toString(), "The CheckIn Date is passed .. ");
        }

        if (roomC.getCapacity() > 0) {

          var orderNumber =   makeOrder(dto, hotelE, roomC,user);
            updateCapacity( dto, roomC);

            roomRepository.save(roomC);

            var hotel = modelMapper.map(hotelE, HotelResponseDto.class);
            var room = modelMapper.map(roomC, RoomResponseDto.class);
            var userResponse = modelMapper.map(user, UserResponseDto.class);

            return OrderRoomResponse.builder()
                    .checkIn(dto.getCheckIn())
                    .checkOut(dto.getCheckOut())
                    .hotel(hotel)
                    .room(room)
                    .roomCapacity(dto.getRoomCapacity())
                    .user(userResponse)
                    .orderNumber(orderNumber)
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

    private String makeOrder(OrderRoomRequest dto, Hotel hotelE, Room roomC, User user) {
        var  orderNumber = generateOrderNumber();
        var orderE = OrderRoom.builder()
                .checkIn(dto.getCheckIn())
                .checkOut(dto.getCheckOut())
                .hotel(hotelE)
                .room(roomC)
                .user(user)
                .roomCapacity(dto.getRoomCapacity())
                .orderNumber(orderNumber)
                .build();
        orderRoomRepository.save(orderE);

        var ordersFromHotel = hotelE.getOrder();
        ordersFromHotel.add(orderE);
        hotelE.setOrder(ordersFromHotel);
        hotelRepository.save(hotelE);

        var ordersFromRoom = roomC.getOrderRooms();
        ordersFromRoom.add(orderE);
        roomC.setOrderRooms(ordersFromRoom);
        roomRepository.save(roomC);

        return orderNumber;
    }



    private void checkExpireOrders(String roomType, String dateFormat) {


        // Fetch the room by type
        Room room = roomRepository.findRoomByTypeIgnoreCase(roomType)
                .orElseThrow(() -> new NotFoundException("No room found for this room type: " + roomType));

        // Fetch all orders for the given room type
        List<OrderRoom> orders = orderRoomRepository.findAllByRoom_TypeIgnoreCase(roomType)
                .orElseThrow(() -> new BadRequestException("This type of room doesn't exist: " + roomType));

        // Filter valid orders based on the check-out date
        List<OrderRoom> validOrders = orders.stream()
                .filter(orderRoom -> !isDatePast(orderRoom.getCheckOut().toString(), dateFormat))
                .collect(Collectors.toList());

        // Check if any orders were removed (i.e., they had a date in the past)
        if (validOrders.size() < orders.size()) {
            // Increase the room's capacity only if some orders were removed
            room.setCapacity(room.getCapacity() + (orders.size() - validOrders.size()));

            // Identify the IDs of orders to be removed from the repository
            List<Long> orderIdsToRemove = orders.stream()
                    .filter(orderRoom -> !validOrders.contains(orderRoom))
                    .map(OrderRoom::getId)
                    .collect(Collectors.toList());

            // Remove the expired orders from the repository
            orderRoomRepository.deleteAllById(orderIdsToRemove);
        }

        // Update the room's orders
        room.setOrderRooms(validOrders);

        // Save the updated room to the database if needed
        roomRepository.save(room);
    }


    public List<OrderRoomResponse> findAllOrderByUserName(String userName) {
        modelMapper.getConfiguration()
                .setMatchingStrategy(MatchingStrategies.STRICT)
                .setPropertyCondition(Conditions.isNotNull());

        List<OrderRoom> orders = orderRoomRepository.findAllByUser_UsernameIgnoreCase(userName).orElseThrow(
                () -> new NotFoundException("No orders found for this user")
        );

        return orders.stream()
                .map(orderRoom -> {
                    OrderRoomResponse orderRoomResponse = modelMapper.map(orderRoom, OrderRoomResponse.class);

                    if (orderRoom.getRoom() != null) {
                        orderRoomResponse.setRoomCapacity(orderRoom.getRoom().getCapacity());
                    }

                    return orderRoomResponse;
                })
                .collect(Collectors.toList());
    }


    private String generateOrderNumber() {
        // Generate a random 6-digit order number
        Random random = new Random();
        int randomNumber = 100_000 + random.nextInt(900_000);
        return String.valueOf(randomNumber);
    }
    @Transactional
    public String deleteOrderRoomByOrderNumber(DeleteRoomRequest deleteRoomRequest) {
        var orderNum = deleteRoomRequest.getOrderNum();
        orderRoomRepository.deleteOrderRoomByOrderNumber(orderNum).orElseThrow(
                () -> new NotFoundException("this order number not in the system." +
                        " contact with the manager "));
        var rooms = roomRepository.findRoomsByHotel_Name(deleteRoomRequest.getHotelName())
                .orElseThrow(()-> new NotFoundException("No found this Room !"));
       var room = rooms.stream()
                .filter(r -> r.getType().equals(deleteRoomRequest.getRoomType()))
                .findFirst().orElseThrow(() -> new NotFoundException("No found this Room !"));
       room.setCapacity(room.getCapacity()+1);
       roomRepository.save(room);

        return "The Order " + orderNum + " delete!";
    }



}
