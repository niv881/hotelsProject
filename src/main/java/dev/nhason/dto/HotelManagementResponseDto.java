package dev.nhason.dto;

import dev.nhason.utils.ImageInfo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HotelManagementResponseDto {
    private HotelResponseDto hotel;
    private AddressResponseDto address;
    private List<ImageInfo> images;
    private List<RoomResponseDto> rooms;
}
