package com.hotrodoan.service;

import com.hotrodoan.model.Equipment;
import com.hotrodoan.model.Room;
import com.hotrodoan.model.Room_Equipment;

public interface Room_EquipmentService {
    Room_Equipment createRoom_Equipment(Room_Equipment room_Equipment);
    Room_Equipment updateRoom_Equipment(Room_Equipment room_Equipment, Long id);
    Room_Equipment getRoom_Equipment(Long id);
    void deleteRoom_Equipment(Long id);
    Room_Equipment getRoom_EquipmentByRoomAndEquipment(Room room, Equipment equipment);
}
