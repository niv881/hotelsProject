package dev.nhason.dto;

import dev.nhason.validation.UniqueEmail;
import dev.nhason.validation.UniqueUsername;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SignUpRequestDto {

    @NotNull
    @NotEmpty
    @Size(min = 2)
    @UniqueUsername
    private String username;
    @NotNull
    @Email
    @NotEmpty
    @UniqueEmail
    private String email;
    @Size(min = 8,max = 20)
    @NotNull(message = "password is mandatory")
    @Pattern(
            regexp = "^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?\\W).{8,20}$",
            message = "password must contain at least 8 characters," +
                    " one or more lower case letters, uppercase letter, symbol, digits"
    )
    private String password;

}
