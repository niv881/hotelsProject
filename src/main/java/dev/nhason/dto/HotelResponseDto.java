package dev.nhason.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HotelResponseDto {
    private long id;
    private String name;
    private String about;
    private String email;
    private String phoneNumber;
    private Double rating;
}
