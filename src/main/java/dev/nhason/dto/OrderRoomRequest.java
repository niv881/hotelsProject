package dev.nhason.dto;

import jakarta.validation.constraints.NotNull;
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
    @NotNull(message = "attribute must to be not null !")
    private Date checkIn;
    @NotNull(message = "attribute must to be not null !")
    private Date checkOut;
    @NotNull(message = "attribute must to be not null !")
    private String hotelName;
    @NotNull(message = "attribute must to be not null !")
    private String roomType;
    @NotNull(message = "attribute must to be not null !")
    private int roomCapacity;
}
