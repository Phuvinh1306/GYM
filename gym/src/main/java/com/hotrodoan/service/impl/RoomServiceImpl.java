package com.hotrodoan.service.impl;

import com.hotrodoan.exception.RoomNotFoundException;
import com.hotrodoan.model.Equipment;
import com.hotrodoan.model.Room;
import com.hotrodoan.repository.RoomRepository;
import com.hotrodoan.service.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class RoomServiceImpl implements RoomService {
    @Autowired
    private RoomRepository roomRepository;

    @Override
    public List<Room> getAllRoom() {
        return roomRepository.findAll();
    }

    @Override
    public Room addRoom(Room room) {
        return roomRepository.save(room);
    }

    @Override
    public Room updateRoom(Room room, Long id) {
        return roomRepository.findById(id).map(po -> {
            po.setName(room.getName());

//            Set<Equipment> equipments = new HashSet<>(room.getEquipment());
//            po.setEquipment(equipments);
            return roomRepository.save(po);
        }).orElseThrow(() -> new RoomNotFoundException("Không tìm thấy phòng tập"));
    }

    @Override
    public void deleteRoom(Long id) {
        if (!roomRepository.existsById(id)) {
            throw new RoomNotFoundException("Không tìm thấy phòng tập");
        }
        roomRepository.deleteById(id);
    }

    @Override
    public Room getRoom(Long id) {
        return roomRepository.findById(id).orElseThrow(() -> new RoomNotFoundException("Không tìm thấy phòng tập"));
    }
}
