package dev.nhason.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderRoomRequest {

    private String checkIn;
    private String checkOut;
    private String hotelName;
    private String roomType;
    private int roomCapacity;

}
