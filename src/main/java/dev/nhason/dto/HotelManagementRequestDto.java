package dev.nhason.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HotelManagementRequestDto {
    private HotelsRequestDto hotel;
    private AddressRequestDto address;
    private List<RoomRequestDto> rooms;
}
