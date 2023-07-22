package dev.nhason.controller;


import dev.nhason.dto.SignInRequestDto;
import dev.nhason.dto.SignUpRequestDto;
import dev.nhason.dto.UserResponseDto;
import dev.nhason.security.JWTProvider;
import dev.nhason.service.UserDetailsServiceImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final UserDetailsServiceImpl userService;
    private  final JWTProvider jwtProvider;
    private final PasswordEncoder passwordEncoder;

    @PostMapping("/signup")
    public ResponseEntity<UserResponseDto> signUp(@RequestBody @Valid SignUpRequestDto dto){
        val response = userService.signUp(dto);

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PostMapping("/signup/manager")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserResponseDto> signUpManager(@RequestBody @Valid SignUpRequestDto dto){
        val response = userService.signUpManager(dto);

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PostMapping("/signin")
    public ResponseEntity<Object> signIn(@RequestBody @Valid SignInRequestDto dto){
        var user = userService.loadUserByUsername(dto.getUsername());
        var savedPassword = user.getPassword();
        var givenPassword = dto.getPassword();
        if (passwordEncoder.matches(givenPassword,savedPassword)){
            var token = jwtProvider.generateToken(user.getUsername());
          return ResponseEntity.ok(Map.of("jwt",token));
        }
        return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }





}
