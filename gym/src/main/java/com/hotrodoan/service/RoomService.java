package com.hotrodoan.service;

import com.hotrodoan.dto.request.Equipment_RoomDTO;
import com.hotrodoan.model.Room;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface RoomService {
    List<Room> getAllRoom();
    Room addRoom(Room room);
    Room updateRoom(Room room, Long id);
    void deleteRoom(Long id);
    Room getRoom(Long id);
    Equipment_RoomDTO getByRoom(Long roomId);
    Page<Room> getAllByKeyword(String name, Pageable pageable);
}
