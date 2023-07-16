package dev.nhason.repository;

import dev.nhason.entity.OrderRoom;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OrderRoomRepository extends JpaRepository<OrderRoom,Long> {
    Optional<OrderRoom>findOrderRoomByHotel_NameIgnoreCaseOrRoom_TypeIgnoreCase(
            String hotelName,String roomType);
}
