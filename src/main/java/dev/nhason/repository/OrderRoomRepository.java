package dev.nhason.repository;

import dev.nhason.entity.OrderRoom;
import org.aspectj.weaver.ast.Or;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface OrderRoomRepository extends JpaRepository<OrderRoom,Long> {
    Optional<List<OrderRoom>> findAllByRoom_TypeIgnoreCase(String roomType);
    Optional<List<OrderRoom>> findAllByUser_UsernameIgnoreCase(String userName);

}
