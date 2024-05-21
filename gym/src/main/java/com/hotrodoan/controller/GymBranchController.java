package com.hotrodoan.controller;

import com.hotrodoan.dto.request.GymBranch_RoomDTO;
import com.hotrodoan.dto.request.Room_Amount;
import com.hotrodoan.dto.response.ResponseMessage;
import com.hotrodoan.model.GymBranch;
import com.hotrodoan.model.GymBranch_Room;
import com.hotrodoan.model.Room;
import com.hotrodoan.service.GymBranchService;
import com.hotrodoan.service.GymBranch_RoomService;
import com.hotrodoan.service.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/gym-branches")
@CrossOrigin(origins = "*")
public class GymBranchController {
    @Autowired
    private GymBranchService gymBranchService;
    @Autowired
    private RoomService roomService;
    @Autowired
    private GymBranch_RoomService gymBranchRoomService;

    @GetMapping("")
    public ResponseEntity<Page<GymBranch>> getAllGymBranches(@RequestParam(defaultValue = "") String name,
                                                            @RequestParam(defaultValue = "") String address,
                                                            @RequestParam(defaultValue = "0") int page,
                                                            @RequestParam(defaultValue = "10") int size,
                                                            @RequestParam(defaultValue = "id") String sortBy,
                                                            @RequestParam(defaultValue = "desc") String order) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Order.desc(sortBy)));
        return new ResponseEntity(gymBranchService.getAllGymBranches(name, address, pageable), HttpStatus.OK);
    }

    @PostMapping("/add")
    public ResponseEntity<GymBranch_RoomDTO> addGymBranch(@RequestBody GymBranch_RoomDTO gymBranchRoomDTO) {
        GymBranch gymBranch = new GymBranch();
        gymBranch.setName(gymBranchRoomDTO.getBranchGymName());
        gymBranch.setAddress(gymBranchRoomDTO.getAddress());
        gymBranch.setManager(gymBranchRoomDTO.getManager());

        GymBranch newGymBranch = gymBranchService.createGymBranch(gymBranch);

        for (Room_Amount roomAmount : gymBranchRoomDTO.getRoomAndAmounts()) {
            GymBranch_Room gymBranchRoom = new GymBranch_Room();
            gymBranchRoom.setGymBranch(newGymBranch);
            gymBranchRoom.setRoom(roomAmount.getRoom());
            gymBranchRoom.setAmount(roomAmount.getAmount());
            gymBranchRoomService.createGymBranch_Room(gymBranchRoom);
        }
        return new ResponseEntity<>(gymBranchRoomDTO, HttpStatus.OK);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<GymBranch> updateGymBranch(@RequestBody GymBranch_RoomDTO gymBranchRoomDTO, @PathVariable Long id){
        GymBranch gymBranch = gymBranchService.getGymBranchById(id);

        gymBranch.setName(gymBranchRoomDTO.getBranchGymName());
        gymBranch.setAddress(gymBranchRoomDTO.getAddress());
        gymBranch.setManager(gymBranchRoomDTO.getManager());
        GymBranch updateGymBranch = gymBranchService.updateGymBranch(gymBranch, id);

        List<GymBranch_Room> gymBranchRooms = gymBranchRoomService.getGymBranchesByGymBranch(gymBranch);
        List<Room_Amount> oldRoomAmounts = new ArrayList<>();
        for (GymBranch_Room gymBranchRoom : gymBranchRooms) {
            Room_Amount room_amount = new Room_Amount();
            room_amount.setRoom(gymBranchRoom.getRoom());
            room_amount.setAmount(gymBranchRoom.getAmount());
            oldRoomAmounts.add(room_amount);
        }
        List<Room_Amount> newRoomAmounts = gymBranchRoomDTO.getRoomAndAmounts();

        if (oldRoomAmounts.size() == newRoomAmounts.size()){
            for (int i = 0; i < oldRoomAmounts.size(); i++) {
                Room_Amount oldRoomAmount = oldRoomAmounts.get(i);
                Room_Amount newRoomAmount = newRoomAmounts.get(i);
                oldRoomAmount.setRoom(newRoomAmount.getRoom());
                oldRoomAmount.setAmount(newRoomAmount.getAmount());

                gymBranchRooms.get(i).setRoom(newRoomAmount.getRoom());
            }
        }

        return new ResponseEntity<>(gymBranchService.updateGymBranch(gymBranch, id), HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ResponseMessage> deleteGymBranch(@PathVariable("id") Long id) {
        gymBranchService.deleteGymBranchById(id);
        return new ResponseEntity<>(new ResponseMessage("deleted"), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<GymBranch> getGymBranch(@PathVariable("id") Long id) {
        return new ResponseEntity<>(gymBranchService.getGymBranchById(id), HttpStatus.OK);
    }
}
