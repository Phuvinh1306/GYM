package com.hotrodoan.repository;

import com.hotrodoan.model.Equipment;
import com.hotrodoan.model.Room;
import com.hotrodoan.model.Room_Equipment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface Room_EquipmentRepository extends JpaRepository<Room_Equipment, Long> {
    Room_Equipment findByRoomAndEquipment(Room room, Equipment equipment);
}
