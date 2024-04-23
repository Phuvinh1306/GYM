package com.hotrodoan.controller;

import com.hotrodoan.dto.response.ResponseMessage;
import com.hotrodoan.model.Room;
import com.hotrodoan.service.RoomService;
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

    @GetMapping("")
    public ResponseEntity<List<Room>> getAllRoom() {
        return new ResponseEntity<>(roomService.getAllRoom(), HttpStatus.OK);
    }

    @PostMapping("/add")
    public ResponseEntity<Room> addRoom(@RequestBody Room room) {
        return new ResponseEntity<>(roomService.addRoom(room), HttpStatus.OK);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Room> updateRoom(@RequestBody Room room, @PathVariable Long id) {
        return new ResponseEntity<>(roomService.updateRoom(room, id), HttpStatus.OK);
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
