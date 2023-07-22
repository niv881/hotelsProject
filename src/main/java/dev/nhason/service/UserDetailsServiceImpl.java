package dev.nhason.service;


import dev.nhason.dto.SignUpRequestDto;
import dev.nhason.dto.UserResponseDto;
import dev.nhason.error.BadRequestException;
import dev.nhason.error.HotelsException;
import dev.nhason.repository.RoleRepository;
import dev.nhason.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final ModelMapper modelMapper;

    @Transactional
    public UserResponseDto signUp(SignUpRequestDto dto) {
        //1) get the user role from role repository:
        val userRole = roleRepository.findByNameIgnoreCase("ROLE_USER")
                .orElseThrow(() -> new HotelsException("Please contact admin"));
        //2) if email/username exists -> Go Sign in (Exception)
        val byUser = userRepository.findByUsernameIgnoreCase(dto.getUsername().trim());
        val byEmail = userRepository.findByEmailIgnoreCase(dto.getEmail().trim());

        if (byEmail.isPresent()) {
            throw new BadRequestException("email", "Email already exists");
        } else if (byUser.isPresent()) {
            throw new BadRequestException("username", "Username already exists");
        }

        //3) val user = new User(... encoded-password )
        var user = dev.nhason.entity.User.builder()
                .id(null)
                .email(dto.getEmail())
                .username(dto.getUsername())
                .password(passwordEncoder.encode(dto.getPassword().trim()))
                .roles(Set.of(userRole))
                .build();
        var savedUser = userRepository.save(user);
        return modelMapper.map(savedUser, UserResponseDto.class);
    }
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        var user = userRepository.findByUsernameIgnoreCase(username).orElseThrow(
                ()-> new UsernameNotFoundException(username)
        );
        var role = user.getRoles().stream().map(r-> new SimpleGrantedAuthority(r.getName())).toList();

        return new User(user.getUsername(),user.getPassword(),role);
    }
}
