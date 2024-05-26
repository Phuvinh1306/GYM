package com.hotrodoan.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hotrodoan.dto.request.GymBranch_RoomDTO;
import com.hotrodoan.dto.request.Room_Amount;
import com.hotrodoan.dto.response.ResponseMessage;
import com.hotrodoan.model.*;
import com.hotrodoan.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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
    @Autowired
    private MemberService memberService;
    @Autowired
    private ImageService imageService;

    @GetMapping("")
    public ResponseEntity<Page<GymBranch>> getAllGymBranches(@RequestParam(defaultValue = "") String keyword,
                                                            @RequestParam(defaultValue = "0") int page,
                                                            @RequestParam(defaultValue = "10") int size,
                                                            @RequestParam(defaultValue = "id") String sortBy,
                                                            @RequestParam(defaultValue = "desc") String order) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Order.desc(sortBy)));
        Page<GymBranch> gymBranches = gymBranchService.getAllGymBranches(keyword, pageable);
        for (GymBranch gymBranch : gymBranches) {
            List<GymBranch_Room> gymBranchRooms = gymBranchRoomService.getGymBranchesByGymBranch(gymBranch);
            int totalMember = memberService.countMemberByGymBranch(gymBranch);
            gymBranch.setTotalMember(totalMember);
        }
        return new ResponseEntity(gymBranches, HttpStatus.OK);
    }

    @PostMapping(value = "/admin/add", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<GymBranch_RoomDTO> addGymBranch(@RequestParam String gymBranch_RoomDTO,
                                                          @RequestParam(value = "file", required = false) MultipartFile file) throws Exception{
        ObjectMapper objectMapper = new ObjectMapper();
        GymBranch_RoomDTO gymBranchRoomDTO = objectMapper.readValue(gymBranch_RoomDTO, GymBranch_RoomDTO.class);
        List<Room_Amount> theSameRoomOnRoomAmounts = new ArrayList<>();
        if (gymBranchService.existsByManager(gymBranchRoomDTO.getManager())) {
            throw new RuntimeException("Manager is duplicated!");
        }
        for (int i = 0; i < gymBranchRoomDTO.getRoomAndAmounts().size()-1; i++) {
            if (gymBranchRoomDTO.getRoomAndAmounts().get(i).getRoom().getId() == gymBranchRoomDTO.getRoomAndAmounts().get(i+1).getRoom().getId()) {
                gymBranchRoomDTO.getRoomAndAmounts().get(i).setAmount(gymBranchRoomDTO.getRoomAndAmounts().get(i).getAmount() + gymBranchRoomDTO.getRoomAndAmounts().get(i+1).getAmount());
                theSameRoomOnRoomAmounts.add(gymBranchRoomDTO.getRoomAndAmounts().get(i));
            }
        }

        if (theSameRoomOnRoomAmounts.size() > 0) {
            throw new RuntimeException("Room is duplicated!");
        }else {
            GymBranch gymBranch = new GymBranch();
            gymBranch.setName(gymBranchRoomDTO.getBranchGymName());
            gymBranch.setAddress(gymBranchRoomDTO.getAddress());
            gymBranch.setManager(gymBranchRoomDTO.getManager());

            if (file != null && !file.isEmpty()) {
                Image image = imageService.saveImage(file);
                gymBranch.setImage(image);
            }

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
    }

    @GetMapping("/{id}/image")
    public ResponseEntity<Resource> viewImage(@PathVariable("id") Long id) throws Exception {
        GymBranch gymBranch = gymBranchService.getGymBranchById(id);
        Image image = gymBranch.getImage();
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(image.getFileType()))
                .body(new ByteArrayResource(image.getData()));
    }

    @PutMapping(value = "/admin/update/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<GymBranch_RoomDTO> updateGymBranch(@RequestParam String gymBranch_RoomDTO,
                                                             @RequestParam(value = "file", required = false) MultipartFile file,
                                                             @PathVariable Long id) throws Exception{
        ObjectMapper objectMapper = new ObjectMapper();
        GymBranch_RoomDTO gymBranchRoomDTO = objectMapper.readValue(gymBranch_RoomDTO, GymBranch_RoomDTO.class);
        List<Room_Amount> theSameRoomOnRoomAmounts = new ArrayList<>();
        if (!gymBranchService.getGymBranchById(id).getManager().equals(gymBranchRoomDTO.getManager()) && gymBranchService.existsByManager(gymBranchRoomDTO.getManager())) {
            throw new RuntimeException("Manager is duplicated!");
        }
        for (int i = 0; i < gymBranchRoomDTO.getRoomAndAmounts().size()-1; i++) {
            if (gymBranchRoomDTO.getRoomAndAmounts().get(i).getRoom().getId() == gymBranchRoomDTO.getRoomAndAmounts().get(i+1).getRoom().getId()) {
                gymBranchRoomDTO.getRoomAndAmounts().get(i).setAmount(gymBranchRoomDTO.getRoomAndAmounts().get(i).getAmount() + gymBranchRoomDTO.getRoomAndAmounts().get(i+1).getAmount());
                theSameRoomOnRoomAmounts.add(gymBranchRoomDTO.getRoomAndAmounts().get(i));
            }
        }
        if (theSameRoomOnRoomAmounts.size() > 0) {
            throw new RuntimeException("Room is duplicated!");
        }
        else {
            GymBranch gymBranch = gymBranchService.getGymBranchById(id);
            gymBranch.setName(gymBranchRoomDTO.getBranchGymName());
            gymBranch.setAddress(gymBranchRoomDTO.getAddress());

            if (!gymBranch.getManager().equals(gymBranchRoomDTO.getManager())) {
                gymBranch.setManager(gymBranchRoomDTO.getManager());
            }

            if (file != null && !file.isEmpty()) {
                String oldImageId = null;
                Image image = imageService.saveImage(file);

                if (gymBranch.getImage() != null && !gymBranch.getImage().equals("")) {
                    oldImageId = gymBranch.getImage().getId();
                }
                gymBranch.setImage(image);
                GymBranch updateGymBranch = gymBranchService.updateGymBranch(gymBranch, id);
                if (oldImageId != null) {
                    imageService.deleteImage(oldImageId);
                }
            }

            List<GymBranch_Room> gymBranchRooms = gymBranchRoomService.getGymBranchesByGymBranch(gymBranch);
            List<Room_Amount> oldRoomAmounts = new ArrayList<>();

            for (GymBranch_Room gymBranchRoom : gymBranchRooms) {
                Room_Amount room_amount = new Room_Amount();
                room_amount.setRoom(gymBranchRoom.getRoom());
                room_amount.setAmount(gymBranchRoom.getAmount());
                oldRoomAmounts.add(room_amount);
            }
            List<Room_Amount> newRoomAmounts = gymBranchRoomDTO.getRoomAndAmounts();

            if (oldRoomAmounts.size() == newRoomAmounts.size()) {
                for (int i = 0; i < oldRoomAmounts.size(); i++) {
                    Room_Amount oldRoomAmount = oldRoomAmounts.get(i);
                    Room_Amount newRoomAmount = newRoomAmounts.get(i);
                    oldRoomAmount.setRoom(newRoomAmount.getRoom());
                    oldRoomAmount.setAmount(newRoomAmount.getAmount());

                    gymBranchRooms.get(i).setRoom(newRoomAmount.getRoom());
                    gymBranchRooms.get(i).setAmount(newRoomAmount.getAmount());
                    gymBranchRoomService.updateGymBranch_Room(gymBranchRooms.get(i), gymBranchRooms.get(i).getId());
                }
            } else if (oldRoomAmounts.size() < newRoomAmounts.size()) {
                for (int i = 0; i < oldRoomAmounts.size(); i++) {
                    Room_Amount oldRoomAmount = oldRoomAmounts.get(i);
                    Room_Amount newRoomAmount = newRoomAmounts.get(i);
                    oldRoomAmount.setRoom(newRoomAmount.getRoom());
                    oldRoomAmount.setAmount(newRoomAmount.getAmount());

                    gymBranchRooms.get(i).setRoom(newRoomAmount.getRoom());
                    gymBranchRooms.get(i).setAmount(newRoomAmount.getAmount());
                    gymBranchRoomService.updateGymBranch_Room(gymBranchRooms.get(i), gymBranchRooms.get(i).getId());
                }
                for (int i = oldRoomAmounts.size(); i < newRoomAmounts.size(); i++) {
                    Room_Amount newRoomAmount = newRoomAmounts.get(i);
                    GymBranch_Room gymBranchRoom = new GymBranch_Room();
                    gymBranchRoom.setGymBranch(gymBranch);
                    gymBranchRoom.setRoom(newRoomAmount.getRoom());
                    gymBranchRoom.setAmount(newRoomAmount.getAmount());
                    gymBranchRoomService.createGymBranch_Room(gymBranchRoom);
                }
            } else {
                for (int i = 0; i < newRoomAmounts.size(); i++) {
                    Room_Amount oldRoomAmount = oldRoomAmounts.get(i);
                    Room_Amount newRoomAmount = newRoomAmounts.get(i);
                    oldRoomAmount.setRoom(newRoomAmount.getRoom());
                    oldRoomAmount.setAmount(newRoomAmount.getAmount());

                    gymBranchRooms.get(i).setRoom(newRoomAmount.getRoom());
                    gymBranchRooms.get(i).setAmount(newRoomAmount.getAmount());
                    gymBranchRoomService.updateGymBranch_Room(gymBranchRooms.get(i), gymBranchRooms.get(i).getId());
                }
                for (int i = newRoomAmounts.size(); i < oldRoomAmounts.size(); i++) {
                    gymBranchRoomService.deleteGymBranch_Room(gymBranchRooms.get(i).getId());
                }
            }
        }
        return new ResponseEntity<>(gymBranchRoomDTO, HttpStatus.OK);
    }

    @DeleteMapping("/admin/delete/{id}")
    public ResponseEntity<ResponseMessage> deleteGymBranch(@PathVariable("id") Long id) {
        gymBranchService.deleteGymBranchById(id);
        return new ResponseEntity<>(new ResponseMessage("deleted"), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<GymBranch> getGymBranch(@PathVariable("id") Long id) {
        return new ResponseEntity<>(gymBranchService.getGymBranchById(id), HttpStatus.OK);
    }

    @GetMapping("admin/{id}")
    public ResponseEntity<GymBranch_RoomDTO> getGymBranchRoom(@PathVariable("id") Long id) {
        GymBranch gymBranch = gymBranchService.getGymBranchById(id);
        GymBranch_RoomDTO gymBranch_RoomDTO = new GymBranch_RoomDTO();
        gymBranch_RoomDTO.setBranchGymName(gymBranch.getName());
        gymBranch_RoomDTO.setAddress(gymBranch.getAddress());
        gymBranch_RoomDTO.setManager(gymBranch.getManager());

        List<GymBranch_Room> gymBranchRooms = gymBranchRoomService.getGymBranchesByGymBranch(gymBranch);
        List<Room_Amount> roomAmounts = new ArrayList<>();
        for (GymBranch_Room gymBranchRoom : gymBranchRooms) {
            Room_Amount room_amount = new Room_Amount();
            room_amount.setRoom(gymBranchRoom.getRoom());
            room_amount.setAmount(gymBranchRoom.getAmount());
            roomAmounts.add(room_amount);
        }

//        int totalMember = memberService.countByGymBranch(gymBranch);

        gymBranch_RoomDTO.setRoomAndAmounts(roomAmounts);

        return new ResponseEntity<>(gymBranch_RoomDTO, HttpStatus.OK);
    }
}
