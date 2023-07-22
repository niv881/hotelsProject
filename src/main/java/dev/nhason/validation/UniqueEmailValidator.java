package dev.nhason.validation;


import dev.nhason.entity.User;
import dev.nhason.repository.UserRepository;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;

import java.util.Optional;

@RequiredArgsConstructor
public class UniqueEmailValidator implements ConstraintValidator<UniqueEmail,String> {
    private final UserRepository userRepository;
    @Override
    public boolean isValid(String email, ConstraintValidatorContext constraintValidatorContext) {
        Optional<User> user = userRepository.findByEmailIgnoreCase(email);
        return user.isEmpty();
    }
}
