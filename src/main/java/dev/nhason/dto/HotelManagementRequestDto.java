package dev.nhason.dto;


import dev.nhason.validation.UniqueHotelDetails;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HotelManagementRequestDto {
    @UniqueHotelDetails
    private HotelsRequestDto hotel;
    private AddressRequestDto address;
    private List<RoomRequestDto> rooms;
}
