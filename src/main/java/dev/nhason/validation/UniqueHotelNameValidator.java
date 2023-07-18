package dev.nhason.validation;

import dev.nhason.dto.HotelResponseDto;
import dev.nhason.dto.HotelsRequestDto;
import dev.nhason.entity.Hotel;
import dev.nhason.repository.HotelRepository;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import java.util.Optional;
@RequiredArgsConstructor
public class UniqueHotelNameValidator implements ConstraintValidator<UniqueHotelDetails, HotelsRequestDto>{
    private final HotelRepository hotelRepository;
    @Override
    public boolean isValid(HotelsRequestDto hotel, ConstraintValidatorContext constraintValidatorContext) {
        Optional<Hotel> hotelE = hotelRepository.findHotelByNameIgnoreCase(hotel.getName());
        return hotelE.isEmpty();
    }
}
