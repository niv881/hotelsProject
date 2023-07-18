package dev.nhason.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HotelsRequestDto {
    @NotNull
    @NotEmpty
    @Size(min = 2)
    private String name;
    private String about;
    private String email;
    private String phoneNumber;
}
