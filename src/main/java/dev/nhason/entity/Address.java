package dev.nhason.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Address {
    @Id
    @GeneratedValue
    private Long id;
    @NotNull
    private String city;
    @NotNull
    private String streetNumber;
    @NotNull
    private String street;
    @NotNull
    private String country;

    @OneToOne
    @JoinColumn(name = "hotel_id",referencedColumnName = "id")
    private Hotel hotel;
}
