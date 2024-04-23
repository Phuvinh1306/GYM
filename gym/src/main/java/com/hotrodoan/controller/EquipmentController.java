package com.hotrodoan.controller;

import com.hotrodoan.dto.response.ResponseMessage;
import com.hotrodoan.model.Equipment;
import com.hotrodoan.service.EquipmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/equipments")
public class EquipmentController {
    @Autowired
    private EquipmentService equipmentService;

    @GetMapping("")
    public ResponseEntity<Page<Equipment>> getAllEquipment(@RequestParam(defaultValue = "0") int page,
                                                           @RequestParam(defaultValue = "10") int size,
                                                           @RequestParam(defaultValue = "id") String sortBy,
                                                           @RequestParam(defaultValue = "desc") String order) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Order.desc(sortBy)));
        return new ResponseEntity<>(equipmentService.getAllEquipment(pageable), HttpStatus.OK);
    }

    @PostMapping("/add")
    public ResponseEntity<Equipment> addEquipment(@RequestBody Equipment equipment) {
        return new ResponseEntity<>(equipmentService.addEquipment(equipment), HttpStatus.OK);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Equipment> updateEquipment(@RequestBody Equipment equipment, @PathVariable Long id) {
        return new ResponseEntity<>(equipmentService.updateEquipment(equipment, id), HttpStatus.OK);
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

    @GetMapping("/search")
    public ResponseEntity<Page<Equipment>> findEquipmentByNameContaining(@RequestParam String name,
                                                                         @RequestParam(defaultValue = "0") int page,
                                                                         @RequestParam(defaultValue = "10") int size,
                                                                         @RequestParam(defaultValue = "id") String sortBy,
                                                                         @RequestParam(defaultValue = "desc") String order) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Order.desc(sortBy)));
        return new ResponseEntity<>(equipmentService.findEquipmentByNameContaining(name, pageable), HttpStatus.OK);
    }
}
