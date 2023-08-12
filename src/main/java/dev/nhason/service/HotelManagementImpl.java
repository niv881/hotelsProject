package dev.nhason.service;

import dev.nhason.dto.*;
import dev.nhason.entity.Address;
import dev.nhason.entity.Hotel;
import dev.nhason.entity.ImageData;
import dev.nhason.entity.Room;
import dev.nhason.error.BadRequestException;
import dev.nhason.error.NotFoundException;
import dev.nhason.repository.AddressRepository;
import dev.nhason.repository.HotelRepository;
import dev.nhason.repository.ImageDataRepository;
import dev.nhason.repository.RoomRepository;
import dev.nhason.utils.ImageInfo;
import dev.nhason.utils.ImageUtil;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

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
    public HotelManagementResponseDto getHotelDetailsByHotelName(String hotelName) {
        Hotel hotelE = hotelRepository.findHotelByNameIgnoreCase(hotelName).orElseThrow(
                ()-> new BadRequestException(hotelName)
        );
        HotelResponseDto hotel = modelMapper.map(hotelE,HotelResponseDto.class);
        Address addressE = addressRepository.findAddressByHotel_Name(hotelName);
        AddressResponseDto address = modelMapper.map(addressE,AddressResponseDto.class);
        List<Room> roomsE = roomRepository.findRoomsByHotel_Name(hotelName).orElseThrow(
                ()->new NotFoundException(hotel.getName(),"no found Room for this hotel")
        );
        List<RoomResponseDto> rooms = roomsE
                .stream()
                .map(room -> modelMapper.map(room,RoomResponseDto.class)).toList();

        List<ImageData> hotelImage = imageDataRepository.findImageDataByHotel_NameIgnoreCase(hotelName);

        return HotelManagementResponseDto.builder()
                .hotel(hotel)
                .address(address)
                .rooms(rooms)
                .build();

    }

    @Override
    public AllHotelsManagementResponseDto getHotelDetailsByHotelAddress(String hotelCountry, String hotelCity) {
        List<Hotel> hotelE = hotelRepository.findAllByAddress_CountryOrAddress_City(hotelCountry, hotelCity);

        List<HotelManagementResponseDto> some = hotelE.stream().map(
                hotel -> {
                    List<Room> roomsE = roomRepository.findRoomsByHotel_Name(hotel.getName()).orElseThrow(
                            ()->new NotFoundException(hotel.getName(),"no found Room for this hotel")
                    );

                    List<RoomResponseDto> rooms = roomsE
                            .stream()
                            .map(room -> modelMapper.map(room,RoomResponseDto.class)).toList();

                    List<ImageData>  imagesFromDataBase = imageDataRepository.findAllByHotel_NameIgnoreCase(hotel.getName()).orElseThrow(
                            ()->new NotFoundException(hotel.getName() , "no picture for this hotel")
                    );
                    List<ImageInfo> hotelImages = imagesFromDataBase
                            .stream().map(imageData -> {
                                byte [] image = ImageUtil.decompressImage(imageData.getImageData()).clone();
                                Long imageId = imageData.getId();
                                ImageInfo imageInfo = ImageInfo.builder()
                                        .image(image)
                                        .id(imageId)
                                        .build();
                                return imageInfo;
                            }).collect(Collectors.toList());

                    return HotelManagementResponseDto.builder()
                          .hotel(modelMapper.map(hotel, HotelResponseDto.class))
                          .address(modelMapper.map(hotel.getAddress(), AddressResponseDto.class))
                          .rooms(rooms)
                          .images(hotelImages)
                          .build();
                }).toList();

        return AllHotelsManagementResponseDto.builder()
                .hotels(some)
                .build();
    }

//    POST METHODS :

    @Override
    public HotelManagementResponseDto createHotel(HotelManagementRequestDto dto) {
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

            var hotel = modelMapper.map(hotelS,HotelResponseDto.class);
            var address = modelMapper.map(addressS,AddressResponseDto.class);
            List<RoomResponseDto> rooms = roomE.stream().map(
                    room -> modelMapper.map(room,RoomResponseDto.class)
            ).toList();
            //TODO : return something else!
            return HotelManagementResponseDto.builder()
                    .hotel(hotel)
                    .address(address)
                    .rooms(rooms)
                    .build();
        }else {
            throw new BadRequestException(dto.getHotel().getName());
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
                    ()->new BadRequestException(dto.getType())
            );

            room.setPrice(dto.getPrice());
            room.setCapacity(dto.getCapacity());
            var saved = roomRepository.save(room);
            return modelMapper.map(saved,RoomResponseDto.class);
        }else {
            throw new NotFoundException(hotelName,"no found Room for this hotel");
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
