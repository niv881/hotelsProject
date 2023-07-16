package dev.nhason.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import java.util.List;
import java.util.Set;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Hotel {
    @Id
    @GeneratedValue
    @NotNull
    private Long id;
    @NotNull
    private String name;
    @NotNull
    @Lob
    @Column(length = 800)
    private String about;
    private String email;
    private String phoneNumber;
    private Double rating = 3.0;

    @OneToOne(mappedBy = "hotel")
    private Address address;

    @OneToMany
    @JoinColumn(name = "room_id",referencedColumnName = "id")
    private Set<Room> rooms;

    @OneToMany
    @JoinColumn(name = "order_id",referencedColumnName = "id")
    private List<OrderRoom> order;

    @OneToMany
    private Set<ImageData> imageData;





}
