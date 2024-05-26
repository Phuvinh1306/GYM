package com.hotrodoan.controller;

import com.hotrodoan.dto.request.Room_Amount;
import com.hotrodoan.dto.request.Room_EquipmentDTO;
import com.hotrodoan.dto.response.ResponseMessage;
import com.hotrodoan.model.*;
import com.hotrodoan.service.EquipmentService;
import com.hotrodoan.service.ImageService;
import com.hotrodoan.service.Room_EquipmentService;
import jakarta.servlet.http.HttpServletRequest;
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
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/equipments")
@CrossOrigin(origins = "*")
public class EquipmentController {
    @Autowired
    private EquipmentService equipmentService;
    @Autowired
    private Room_EquipmentService room_EquipmentService;
    @Autowired
    private ImageService imageService;

//    @GetMapping("")
//    public ResponseEntity<Page<Equipment>> getAllEquipment(@RequestParam(defaultValue = "0") int page,
//                                                           @RequestParam(defaultValue = "10") int size,
//                                                           @RequestParam(defaultValue = "id") String sortBy,
//                                                           @RequestParam(defaultValue = "desc") String order) {
//        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Order.desc(sortBy)));
//        return new ResponseEntity<>(equipmentService.getAllEquipment(pageable), HttpStatus.OK);
//    }

//    @PostMapping("/add")
//    public ResponseEntity<Room_EquipmentDTO> addEquipment(@RequestBody Room_EquipmentDTO roomEquipmentDTO) {
//        Equipment equipment = new Equipment();
//        equipment.setName(roomEquipmentDTO.getName());
//        equipment.setPrice(roomEquipmentDTO.getPrice());
//        equipment.setMadein(roomEquipmentDTO.getMadein());
//        equipment.setImage(roomEquipmentDTO.getImage());
//        equipment.setEquipType(roomEquipmentDTO.getEquipType());
//        Equipment newEquipment = equipmentService.addEquipment(equipment);
//
//        List<Room_Amount> roomAmounts = roomEquipmentDTO.getRoomAmounts();
//
//        for (Room_Amount roomAmount : roomAmounts) {
//            Room_Equipment roomEquipment = new Room_Equipment();
//            roomEquipment.setEquipment(newEquipment);
//            roomEquipment.setRoom(roomAmount.getRoom());
//            roomEquipment.setEquipment(newEquipment);
//            roomEquipment.setQuantity(roomAmount.getAmount());
//            room_EquipmentService.createRoom_Equipment(roomEquipment);
//        }
//        return new ResponseEntity<>(roomEquipmentDTO, HttpStatus.OK);
//    }

    @PostMapping(value = "/admin/add", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Equipment> addEquipment(@RequestParam("name") String name,
                                                 @RequestParam("price") double price,
                                                 @RequestParam("madein") String madein,
                                                 @RequestParam(value = "file", required = false) MultipartFile file,
                                                 @RequestParam EquipType equipType) throws Exception{
        Equipment equipment = new Equipment();
        equipment.setName(name);
        equipment.setPrice(price);
        equipment.setMadein(madein);
        equipment.setEquipType(equipType);

        if (file != null && !file.isEmpty()) {
            Image image = imageService.saveImage(file);
            equipment.setImage(image);
        }
        return new ResponseEntity<>(equipmentService.addEquipment(equipment), HttpStatus.OK);
    }

//    @PutMapping("/update/{id}")
//    public ResponseEntity<Room_EquipmentDTO> updateEquipment(@RequestBody Room_EquipmentDTO roomEquipmentDTO, @PathVariable Long id) {
//        Equipment equipment = equipmentService.getEquipment(id);
////        Room_Equipment roomEquipment = room_EquipmentService.getRoom_EquipmentByRoomAndEquipment(room, equipment);
//        equipment.setName(roomEquipmentDTO.getName());
//        equipment.setPrice(roomEquipmentDTO.getPrice());
//        equipment.setMadein(roomEquipmentDTO.getMadein());
//        equipment.setImage(roomEquipmentDTO.getImage());
//        equipment.setEquipType(roomEquipmentDTO.getEquipType());
//        Room room = roomEquipmentDTO.getRoom();

//        roomEquipment.setEquipment(equipment);
//        roomEquipment.setRoom(room);
//        roomEquipment.setQuantity(roomEquipmentDTO.getQuantity());
//        return new ResponseEntity<>(roomEquipmentDTO, HttpStatus.OK);
//    }

    @PutMapping(value = "/admin/update/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Equipment> updateEquipment(@RequestParam("name") String name,
                                                     @RequestParam("price") double price,
                                                     @RequestParam("madein") String madein,
                                                     @RequestParam(value = "file", required = false) MultipartFile file,
                                                     @RequestParam EquipType equipType,
                                                     @PathVariable Long id) throws Exception {
        Equipment equipment = equipmentService.getEquipment(id);
        String imageId = null;
        equipment.setName(name);
        equipment.setPrice(price);
        equipment.setMadein(madein);
        equipment.setEquipType(equipType);

        if (file != null && !file.isEmpty()) {
            Image image = imageService.saveImage(file);
            if (equipment.getImage() != null && !equipment.getImage().equals("")) {
                imageId = equipment.getImage().getId();
                equipment.setImage(image);
                Equipment updatedEquipment = equipmentService.updateEquipment(equipment, id);
                imageService.deleteImage(imageId);
                return new ResponseEntity<>(updatedEquipment, HttpStatus.OK);
            }else {
                equipment.setImage(image);
                Equipment updatedEquipment = equipmentService.updateEquipment(equipment, id);
                return new ResponseEntity<>(updatedEquipment, HttpStatus.OK);
            }
        }
        return new ResponseEntity<>(equipmentService.updateEquipment(equipment, id), HttpStatus.OK);
    }
    @GetMapping("/{id}/image")
    public ResponseEntity<Resource> viewImage(@PathVariable("id") Long id) throws Exception {
        Equipment equipment = equipmentService.getEquipment(id);
        Image image = equipment.getImage();
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(image.getFileType()))
                .body(new ByteArrayResource(image.getData()));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteEquipment(@PathVariable("id") Long id) {
        equipmentService.deleteEquipment(id);
        return new ResponseEntity<>(new ResponseMessage("deleted"), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Equipment> getEquipment(@PathVariable("id") Long id) {
        return new ResponseEntity<>(equipmentService.getEquipment(id), HttpStatus.OK);
    }

    @GetMapping("")
    public ResponseEntity<Page<Equipment>> findEquipmentByNameContaining(@RequestParam(defaultValue = "") String name,
                                                                         @RequestParam(defaultValue = "0") int page,
                                                                         @RequestParam(defaultValue = "10") int size,
                                                                         @RequestParam(defaultValue = "id") String sortBy,
                                                                         @RequestParam(defaultValue = "desc") String order) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Order.desc(sortBy)));
        return new ResponseEntity<>(equipmentService.findEquipmentByNameContaining(name, pageable), HttpStatus.OK);
    }
}
