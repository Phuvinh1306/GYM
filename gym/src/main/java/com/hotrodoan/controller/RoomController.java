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

import java.util.ArrayList;
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

    @PostMapping("/admin/add")
    public ResponseEntity<Equipment_RoomDTO> addRoom(@RequestBody Equipment_RoomDTO equipmentRoomDTO) {
        Room room = new Room();
        room.setName(equipmentRoomDTO.getName());
        Room newRoom = roomService.addRoom(room);
        List<Equipment_Amount> equipmentAmounts = equipmentRoomDTO.getEquipmentAmounts();
        for (Equipment_Amount equipmentAmount : equipmentAmounts) {
            Equipment equipment = equipmentAmount.getEquipment();

            Room_Equipment roomEquipment = new Room_Equipment();
            roomEquipment.setRoom(newRoom);
            roomEquipment.setEquipment(equipment);
            roomEquipment.setQuantity(equipmentAmount.getAmount());
            room_equipmentService.createRoom_Equipment(roomEquipment);
        }
        return new ResponseEntity<>(equipmentRoomDTO, HttpStatus.OK);
    }

    @GetMapping("/admin/{id}")
    public ResponseEntity<Equipment_RoomDTO> getRoomAdmin(@PathVariable("id") Long id) {
        Equipment_RoomDTO equipmentRoomDTO = roomService.getByRoom(id);
        return new ResponseEntity<>(equipmentRoomDTO, HttpStatus.OK);
    }

    @PutMapping("/admin/update/{id}")
    public ResponseEntity<Equipment_RoomDTO> updateRoom(@RequestBody Equipment_RoomDTO equipmentRoomDTO, @PathVariable Long id) {
        Equipment_RoomDTO oldEquipment_RoomDTO = roomService.getByRoom(id);
        Room room = roomService.getRoom(id);
        room.setName(equipmentRoomDTO.getName());
        Room updatedRoom = roomService.updateRoom(room, id);

        List<Equipment_Amount> newEquipmentAmounts = equipmentRoomDTO.getEquipmentAmounts();
        List<Equipment_Amount> oldEquipmentAmounts = oldEquipment_RoomDTO.getEquipmentAmounts();

        if (newEquipmentAmounts.size() > oldEquipmentAmounts.size()) {
            for (int i = 0; i < oldEquipmentAmounts.size(); i++) {
                Equipment_Amount oldEquipmentAmount = oldEquipmentAmounts.get(i);
                Equipment_Amount newEquipmentAmount = newEquipmentAmounts.get(i);

                Room_Equipment roomEquipment = room_equipmentService.getRoom_EquipmentByRoomAndEquipment(updatedRoom, oldEquipmentAmount.getEquipment());

                roomEquipment.setRoom(updatedRoom);
                roomEquipment.setEquipment(newEquipmentAmount.getEquipment());
                roomEquipment.setQuantity(newEquipmentAmount.getAmount());
                room_equipmentService.updateRoom_Equipment(roomEquipment, roomEquipment.getId());
            }

            for (int i = oldEquipmentAmounts.size(); i < newEquipmentAmounts.size(); i++) {
                Room newRoom = roomService.getRoom(id);
                Equipment newEquipment = newEquipmentAmounts.get(i).getEquipment();
                int quantity = newEquipmentAmounts.get(i).getAmount();
                Room_Equipment roomEquipment = new Room_Equipment();
                roomEquipment.setRoom(newRoom);
                roomEquipment.setEquipment(newEquipment);
                roomEquipment.setQuantity(quantity);
                room_equipmentService.createRoom_Equipment(roomEquipment);
            }
        } else if (newEquipmentAmounts.size() == oldEquipmentAmounts.size()) {
            for (int i = 0; i < oldEquipmentAmounts.size(); i++) {
                Equipment_Amount oldEquipmentAmount = oldEquipmentAmounts.get(i);
                Equipment_Amount newEquipmentAmount = newEquipmentAmounts.get(i);

                Room_Equipment roomEquipment = room_equipmentService.getRoom_EquipmentByRoomAndEquipment(updatedRoom, oldEquipmentAmount.getEquipment());

                roomEquipment.setRoom(updatedRoom);
                roomEquipment.setEquipment(newEquipmentAmount.getEquipment());
                roomEquipment.setQuantity(newEquipmentAmount.getAmount());
                room_equipmentService.updateRoom_Equipment(roomEquipment, roomEquipment.getId());
            }
        } else {
            for (int i = 0; i < newEquipmentAmounts.size(); i++) {
                Equipment_Amount oldEquipmentAmount = oldEquipmentAmounts.get(i);
                Equipment_Amount newEquipmentAmount = newEquipmentAmounts.get(i);

                Room_Equipment roomEquipment = room_equipmentService.getRoom_EquipmentByRoomAndEquipment(updatedRoom, oldEquipmentAmount.getEquipment());

                roomEquipment.setRoom(updatedRoom);
                roomEquipment.setEquipment(newEquipmentAmount.getEquipment());
                roomEquipment.setQuantity(newEquipmentAmount.getAmount());
                room_equipmentService.updateRoom_Equipment(roomEquipment, roomEquipment.getId());
            }

            for (int i = newEquipmentAmounts.size(); i < oldEquipmentAmounts.size(); i++) {
                Equipment_Amount oldEquipmentAmount = oldEquipmentAmounts.get(i);
                Room_Equipment roomEquipment = room_equipmentService.getRoom_EquipmentByRoomAndEquipment(updatedRoom, oldEquipmentAmount.getEquipment());
                room_equipmentService.deleteRoom_Equipment(roomEquipment.getId());
            }
        }

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
