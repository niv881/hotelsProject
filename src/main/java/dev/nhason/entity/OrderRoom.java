package dev.nhason.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class OrderRoom {

    @Id
    @GeneratedValue
    private Long id;

    private LocalDate checkIn;
    private LocalDate checkOut;

    @OneToOne(mappedBy = "order")
    private Hotel hotel;

    @OneToOne(mappedBy = "order")
    private Room room;


}
