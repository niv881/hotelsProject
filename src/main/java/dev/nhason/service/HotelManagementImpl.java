package dev.nhason.service;

import dev.nhason.dto.*;
import dev.nhason.entity.Address;
import dev.nhason.entity.Hotel;
import dev.nhason.entity.ImageData;
import dev.nhason.entity.Room;
import dev.nhason.repository.AddressRepository;
import dev.nhason.repository.HotelRepository;
import dev.nhason.repository.ImageDataRepository;
import dev.nhason.repository.RoomRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class HotelManagementImpl implements HotelManagement {
    private final AddressRepository addressRepository;
    private final HotelRepository hotelRepository;
    private final RoomRepository roomRepository;
    private final ImageDataRepository imageDataRepository;
    private final ModelMapper modelMapper;


//    GET METHODS :
    @Override
    public HotelManagementDto getHotelDetailsByHotelName(String hotelName) {
        Hotel hotelE = hotelRepository.findHotelByNameIgnoreCase(hotelName).orElseThrow(
                //TODO: message if don't find the hotel
                RuntimeException::new
        );
        HotelResponseDto hotel = modelMapper.map(hotelE,HotelResponseDto.class);
        Address addressE = addressRepository.findAddressByHotel_Name(hotelName);
        AddressResponseDto address = modelMapper.map(addressE,AddressResponseDto.class);
        List<Room> roomsE = roomRepository.findRoomsByHotel_Name(hotelName).orElseThrow(
                //TODO: message if don't find the room
                RuntimeException::new
        );
        List<RoomResponseDto> rooms = roomsE
                .stream()
                .map(room -> modelMapper.map(room,RoomResponseDto.class)).toList();

        List<ImageData> hotelImage = imageDataRepository.findImageDataByHotel_NameIgnoreCase(hotelName);

        return HotelManagementDto.builder()
                .hotel(hotel)
                .address(address)
                .rooms(rooms)
                .build();

    }

    @Override
    public HotelsManagementResponseDto getHotelDetailsByHotelAddress(String hotelCountry, String hotelCity) {
        List<Hotel> hotelE = hotelRepository.findAllByAddress_CountryOrAddress_City(hotelCountry, hotelCity);

        List<HotelManagementDto> some = hotelE.stream().map(
                hotel -> {
                    List<Room> roomsE = roomRepository.findRoomsByHotel_Name(hotel.getName()).orElseThrow(
                            //TODO: message if don't find the room
                            RuntimeException::new
                    );

                    List<RoomResponseDto> rooms = roomsE
                            .stream()
                            .map(room -> modelMapper.map(room,RoomResponseDto.class)).toList();

                    return HotelManagementDto.builder()
                          .hotel(modelMapper.map(hotel, HotelResponseDto.class))
                          .address(modelMapper.map(hotel.getAddress(), AddressResponseDto.class))
                          .rooms(rooms)
                          .build();
                }).toList();

        return HotelsManagementResponseDto.builder()
                .hotels(some)
                .build();
    }

//    POST METHODS :

    @Override
    public Boolean createHotel(HotelManagementRequestDto dto) {
        if (!checkIfHotelExists(dto.getHotel().getName())){
            var hotelE = modelMapper.map(dto.getHotel(),Hotel.class);
            var hotelS = hotelRepository.save(hotelE);
            var addressE = modelMapper.map(dto.getAddress(),Address.class);
            var addressS = addressRepository.save(addressE);
            addressS.setHotel(hotelS);
            List<Room> roomE = dto.getRooms()
                    .stream()
                    .map(room -> modelMapper.map(room, Room.class)).toList();
            roomE.forEach(room -> room.setHotel(hotelS));
            roomRepository.saveAll(roomE);
            //TODO : return something else!
            return true;
        }else {
            //TODO : message if hotel Already Exists;
            throw new RuntimeException();
        }

    }

//    PUT METHODS :

    @Override
    public HotelResponseDto updateHotelDetails(HotelsRequestDto dto) {

        if(checkIfHotelExists(dto.getName())){
          Hotel hotel = hotelRepository.findHotelByNameIgnoreCase(dto.getName()).orElseThrow();
           return checkFieldAndUpdateHotelResponse(dto, hotel);

        }else {
            throw new RuntimeException();
        }
    }

    @Override
    public RoomResponseDto updateHotelRoom(RoomRequestDto dto, String hotelName) {
        if (checkIfHotelExists(hotelName)){
            Room room = roomRepository.findRoomByTypeAndHotel_Name(dto.getType(),hotelName).orElseThrow(

            );

            room.setPrice(dto.getPrice());
            room.setCapacity(dto.getCapacity());
            var saved = roomRepository.save(room);
            return modelMapper.map(saved,RoomResponseDto.class);
        }else {
            throw new RuntimeException("no resource found !");
        }
    }


//    HELPER METHODS :

    private boolean checkIfHotelExists(String hotelName) {
        return hotelRepository.findHotelByNameIgnoreCase(hotelName).isPresent();
    }

    private HotelResponseDto checkFieldAndUpdateHotelResponse(HotelsRequestDto hotelReq, Hotel hotel) {
        String name = hotelReq.getName();
        String about = hotelReq.getAbout();
        String email = hotelReq.getEmail();
        String phoneNumber = hotelReq.getPhoneNumber();

        hotel.setName(name);
        hotel.setAbout(about);
        hotel.setEmail(email);
        hotel.setPhoneNumber(phoneNumber);

        var saved = hotelRepository.save(hotel);

        return modelMapper.map(saved,HotelResponseDto.class);

    }

}
