package com.hotrodoan.service;

import com.hotrodoan.dto.request.Equipment_RoomDTO;
import com.hotrodoan.model.Room;

import java.util.List;

public interface RoomService {
    List<Room> getAllRoom();
    Room addRoom(Room room);
    Room updateRoom(Room room, Long id);
    void deleteRoom(Long id);
    Room getRoom(Long id);
    Equipment_RoomDTO getByRoom(Long roomId);
}
