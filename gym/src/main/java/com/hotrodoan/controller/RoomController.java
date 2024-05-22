package com.hotrodoan.controller;

import com.hotrodoan.dto.request.Equipment_Amount;
import com.hotrodoan.dto.request.Equipment_RoomDTO;
import com.hotrodoan.dto.response.ResponseMessage;
import com.hotrodoan.model.Equipment;
import com.hotrodoan.model.Room;
import com.hotrodoan.model.Room_Equipment;
import com.hotrodoan.service.RoomService;
import com.hotrodoan.service.Room_EquipmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/rooms")
@CrossOrigin(origins = "*")
public class RoomController {
    @Autowired
    private RoomService roomService;
    @Autowired
    private Room_EquipmentService room_equipmentService;

    @GetMapping("")
    public ResponseEntity<List<Room>> getAllRoom() {
        return new ResponseEntity<>(roomService.getAllRoom(), HttpStatus.OK);
    }

    @PostMapping("/add")
    public ResponseEntity<Equipment_RoomDTO> addRoom(@RequestBody Equipment_RoomDTO equipmentRoomDTO) {
        Room room = new Room();
        room.setName(equipmentRoomDTO.getName());
        Room newRoom = roomService.addRoom(room);
        List<Equipment_Amount> equipmentAmounts = equipmentRoomDTO.getEquipmentAmounts();
        for (Equipment_Amount equipmentAmount : equipmentAmounts) {
            Equipment equipment = new Equipment();
            equipment.setName(equipmentAmount.getEquipment().getName());
            equipment.setPrice(equipmentAmount.getEquipment().getPrice());
            equipment.setMadein(equipmentAmount.getEquipment().getMadein());
            equipment.setImage(equipmentAmount.getEquipment().getImage());
            equipment.setEquipType(equipmentAmount.getEquipment().getEquipType());
            Room_Equipment roomEquipment = new Room_Equipment();
            roomEquipment.setRoom(newRoom);
            roomEquipment.setEquipment(equipment);
            room_equipmentService.createRoom_Equipment(roomEquipment);
        }
        return new ResponseEntity<>(equipmentRoomDTO, HttpStatus.OK);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Equipment_RoomDTO> updateRoom(@RequestBody Equipment_RoomDTO equipmentRoomDTO, @PathVariable Long id) {
        Room room = roomService.getRoom(id);
        room.setName(equipmentRoomDTO.getName());
        return new ResponseEntity<>(equipmentRoomDTO, HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteRoom(@PathVariable("id") Long id) {
        roomService.deleteRoom(id);
        return new ResponseEntity<>(new ResponseMessage("deleted"), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Room> getRoom(@PathVariable("id") Long id) {
        return new ResponseEntity<>(roomService.getRoom(id), HttpStatus.OK);
    }
}
