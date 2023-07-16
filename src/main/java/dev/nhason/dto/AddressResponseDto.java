package dev.nhason.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AddressResponseDto {
    private long id;
    private String country;
    private String city;
    private String street;
    private String streetNumber;
}
