package com.hotrodoan.service.impl;

import com.hotrodoan.model.Equipment;
import com.hotrodoan.model.Room;
import com.hotrodoan.model.Room_Equipment;
import com.hotrodoan.repository.Room_EquipmentRepository;
import com.hotrodoan.service.Room_EquipmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class Room_EquipmentServiceImpl implements Room_EquipmentService {
    @Autowired
    private Room_EquipmentRepository room_EquipmentRepository;

    @Override
    public Room_Equipment createRoom_Equipment(Room_Equipment room_Equipment) {
        return room_EquipmentRepository.save(room_Equipment);
    }

    @Override
    public Room_Equipment updateRoom_Equipment(Room_Equipment room_Equipment, Long id) {
        return room_EquipmentRepository.findById(id).map(re -> {
            re.setRoom(room_Equipment.getRoom());
            re.setEquipment(room_Equipment.getEquipment());
            re.setQuantity(room_Equipment.getQuantity());
            return room_EquipmentRepository.save(re);
        }).orElseGet(() -> room_EquipmentRepository.save(room_Equipment));
    }

    @Override
    public Room_Equipment getRoom_Equipment(Long id) {
        return room_EquipmentRepository.findById(id).orElseThrow(() -> new RuntimeException("Room equipment not found"));
    }

    @Override
    public void deleteRoom_Equipment(Long id) {
        room_EquipmentRepository.deleteById(id);
    }

    @Override
    public Room_Equipment getRoom_EquipmentByRoomAndEquipment(Room room, Equipment equipment) {
        return room_EquipmentRepository.findByRoomAndEquipment(room, equipment);
    }

    @Override
    public List<Room_Equipment> getRoom_EquipmentByRoom(Room room) {
        return room_EquipmentRepository.findByRoom(room);
    }

    @Override
    public Integer countUsedEachEquipment(Equipment equipment) {
        return room_EquipmentRepository.countUsedEachEquipment(equipment);
    }
}
