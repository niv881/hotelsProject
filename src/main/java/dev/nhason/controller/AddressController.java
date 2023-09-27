package dev.nhason.controller;

import dev.nhason.dto.*;
import dev.nhason.service.AddressService;
import dev.nhason.service.HotelManagement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class AddressController {
    private final AddressService addressService;

    @GetMapping("/all_address")
    public ResponseEntity<List<AddressResponseDto>> getDetailsByHotelAddress(){
        return ResponseEntity.ok(addressService.getAllAddress());
    }




}
