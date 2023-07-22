package dev.nhason.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.List;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Room {
    @Id
    @GeneratedValue
    private Long id;

    @NotNull
    private String type;
    @NotNull
    private Double price;
    @NotNull
    private int capacity;

    @ManyToOne
    @JoinColumn(name = "hotel_id",referencedColumnName = "id")
    private Hotel hotel;

    @OneToMany
    @JoinColumn(name = "order_room_id",referencedColumnName = "id")
    private List<OrderRoom> orderRooms;


}
