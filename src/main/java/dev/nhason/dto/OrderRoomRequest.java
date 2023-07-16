package dev.nhason.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderRoomRequest {

    private Date checkIn;
    private Date checkOut;
    private String hotelName;
    private String roomType;
    private int roomCapacity;
}
