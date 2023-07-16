package dev.nhason.dto;

import dev.nhason.entity.Address;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HotelWithAddress {
    private long id;
    private String name;
    private String about;
    private String email;
    private String phoneNumber;
    private AddressResponseDto address;
}
