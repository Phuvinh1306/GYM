package com.hotrodoan.controller;

import com.hotrodoan.dto.response.ResponseMessage;
import com.hotrodoan.model.Employee;
import com.hotrodoan.model.EquipType;
import com.hotrodoan.service.EquipTypeService;
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
@RequestMapping("/equip-types")
public class EquipTypeController {
    @Autowired
    private EquipTypeService equipTypeService;

    @GetMapping("")
    public ResponseEntity<Page<EquipType>> getAllEquipType(@RequestParam(defaultValue = "0") int page,
                                                           @RequestParam(defaultValue = "10") int size,
                                                           @RequestParam(defaultValue = "id") String sortBy,
                                                           @RequestParam(defaultValue = "desc") String order) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Order.desc(sortBy)));
        return new ResponseEntity<>(equipTypeService.getAllEquipType(pageable), HttpStatus.OK);
    }

    @PostMapping("/add")
    public ResponseEntity<EquipType> addEquipType(@RequestBody EquipType equipType) {
        return new ResponseEntity<>(equipTypeService.addEquipType(equipType), HttpStatus.OK);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<EquipType> updateEquipType(@RequestBody EquipType equipType, @PathVariable Long id) {
        return new ResponseEntity<>(equipTypeService.updateEquipType(equipType, id), HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteEquipType(@PathVariable("id") Long id) {
        equipTypeService.deleteEquipType(id);
        return new ResponseEntity<>(new ResponseMessage("deleted"), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EquipType> getEquipType(@PathVariable("id") Long id) {
        return new ResponseEntity<>(equipTypeService.getEquipType(id), HttpStatus.OK);
    }

    @GetMapping("/search")
    public ResponseEntity<Page<EquipType>> findEmployeesByNameContaining(@RequestParam String name,
                                                                        @RequestParam(defaultValue = "0") int page,
                                                                        @RequestParam(defaultValue = "10") int size,
                                                                        @RequestParam(defaultValue = "id") String sortBy,
                                                                        @RequestParam(defaultValue = "desc") String order) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Order.desc(sortBy)));
        return new ResponseEntity<>(equipTypeService.findEquipTypesByNameContaining(name, pageable), HttpStatus.OK);
    }
}
