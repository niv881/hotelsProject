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
public class DeleteRoomRequest {
    @NotNull(message = "attribute must to be not null !")
    private String orderNum;
    @NotNull(message = "attribute must to be not null !")
    private String roomType;
    @NotNull(message = "attribute must to be not null !")
    private String hotelName;
}
