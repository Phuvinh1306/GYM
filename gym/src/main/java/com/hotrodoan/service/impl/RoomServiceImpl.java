package com.hotrodoan.service.impl;

import com.hotrodoan.dto.request.Equipment_Amount;
import com.hotrodoan.dto.request.Equipment_RoomDTO;
import com.hotrodoan.exception.RoomNotFoundException;
import com.hotrodoan.model.Equipment;
import com.hotrodoan.model.Room;
import com.hotrodoan.model.Room_Equipment;
import com.hotrodoan.repository.RoomRepository;
import com.hotrodoan.service.RoomService;
import com.hotrodoan.service.Room_EquipmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class RoomServiceImpl implements RoomService {
    @Autowired
    private RoomRepository roomRepository;
    @Autowired
    private Room_EquipmentService room_equipmentService;

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
            po.setImage(room.getImage());
            po.setDescription(room.getDescription());

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

    @Override
    public Equipment_RoomDTO getByRoom(Long roomId) {
        Room room = roomRepository.findById(roomId).orElseThrow(() -> new RoomNotFoundException("Không tìm thấy phòng tập"));
        List<Room_Equipment> roomEquipments = room_equipmentService.getRoom_EquipmentByRoom(room);
        Equipment_RoomDTO equipmentRoomDTO = new Equipment_RoomDTO();
        equipmentRoomDTO.setDescription(room.getDescription());
        equipmentRoomDTO.setName(room.getName());
        List<Equipment_Amount> equipmentAmounts = new ArrayList<>();
        for (Room_Equipment roomEquipment : roomEquipments) {
            Equipment_Amount equipmentAmount = new Equipment_Amount();
            equipmentAmount.setEquipment(roomEquipment.getEquipment());
            equipmentAmount.setAmount(roomEquipment.getQuantity());
            equipmentAmounts.add(equipmentAmount);
        }
        equipmentRoomDTO.setEquipmentAmounts(equipmentAmounts);
        return equipmentRoomDTO;
    }

    @Override
    public Page<Room> getAllByKeyword(String name, Pageable pageable) {
        return roomRepository.findAllByKeyword(name, pageable);
    }
}
