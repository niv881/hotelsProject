package dev.nhason.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HotelManagementDto {
    private HotelResponseDto hotel;
    private AddressResponseDto address;
    private List<RoomResponseDto> rooms;
}
