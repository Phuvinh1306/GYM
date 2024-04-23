package com.hotrodoan.controller;

import com.hotrodoan.dto.response.ResponseMessage;
import com.hotrodoan.model.Employee;
import com.hotrodoan.model.Package;
import com.hotrodoan.service.PackageService;
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
@RequestMapping("/packages")
@CrossOrigin(origins = "*")
public class PackageController {
    @Autowired
    private PackageService packageService;

    @GetMapping("")
    public ResponseEntity<Page<Package>> getAllPackage(@RequestParam(defaultValue = "0") int page,
                                                       @RequestParam(defaultValue = "10") int size,
                                                       @RequestParam(defaultValue = "id") String sortBy,
                                                       @RequestParam(defaultValue = "desc") String order) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Order.desc(sortBy)));
        return new ResponseEntity<>(packageService.getAllPackage(pageable), HttpStatus.OK);
    }

    @PostMapping("/add")
    public ResponseEntity<Package> addMember(@RequestBody Package pack) {
        return new ResponseEntity<>(packageService.addPackage(pack), HttpStatus.OK);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Package> updatePackage(@RequestBody Package pack, @PathVariable Long id){
        return new ResponseEntity<>(packageService.updatePackage(pack, id), HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deletePackage(@PathVariable("id") Long id) {
        packageService.deletePackage(id);
        return new ResponseEntity<>(new ResponseMessage("deleted"), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Package> getPackage(@PathVariable("id") Long id) {
        return new ResponseEntity<>(packageService.getPackage(id), HttpStatus.OK);
    }

    @GetMapping("/search")
    public ResponseEntity<Page<Package>> findPackagesByNameContaining(@RequestParam String name,
                                                                        @RequestParam(defaultValue = "0") int page,
                                                                        @RequestParam(defaultValue = "10") int size,
                                                                        @RequestParam(defaultValue = "id") String sortBy,
                                                                        @RequestParam(defaultValue = "desc") String order) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Order.desc(sortBy)));
        return new ResponseEntity<>(packageService.findPackagesByNameContaining(name, pageable), HttpStatus.OK);
    }
}
