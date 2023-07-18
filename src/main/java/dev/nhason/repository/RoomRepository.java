package dev.nhason.repository;

import dev.nhason.entity.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;


public interface RoomRepository extends JpaRepository<Room,Long> {
    Optional<List<Room>> findRoomsByHotel_Name(String name);
    Optional<Room> findRoomByTypeAndHotel_Name(String type,String hotelName);

}
