package com.hotrodoan.repository;

import com.hotrodoan.model.Equipment;
import com.hotrodoan.model.Room;
import com.hotrodoan.model.Room_Equipment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface Room_EquipmentRepository extends JpaRepository<Room_Equipment, Long> {
    Room_Equipment findByRoomAndEquipment(Room room, Equipment equipment);
    List<Room_Equipment> findByRoom(Room room);
    @Query("SELECT COALESCE(SUM(r.quantity), 0) FROM Room_Equipment r WHERE r.equipment = ?1")
    Integer countUsedEachEquipment(Equipment equipment);
}
