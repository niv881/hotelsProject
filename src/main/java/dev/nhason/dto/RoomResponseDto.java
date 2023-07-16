package dev.nhason.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RoomResponseDto {
    private long id;
    private String type;
    private Double price;
    private Integer capacity;
}
