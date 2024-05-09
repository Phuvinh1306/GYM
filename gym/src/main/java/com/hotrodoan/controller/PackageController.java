package com.hotrodoan.controller;

import com.hotrodoan.dto.response.ResponseMessage;
import com.hotrodoan.model.Employee;
import com.hotrodoan.model.Image;
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
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;

@RestController
@RequestMapping("/packages")
@CrossOrigin(origins = "*")
public class PackageController {
    private static final Path UPLOAD_DIR = Paths.get(System.getProperty("user.dir"), "/src/main/resources/static/images/package");

    @Autowired
    private PackageService packageService;

//    @GetMapping("")
//    public ResponseEntity<Page<Package>> getAllPackage(@RequestParam(defaultValue = "0") int page,
//                                                       @RequestParam(defaultValue = "10") int size,
//                                                       @RequestParam(defaultValue = "id") String sortBy,
//                                                       @RequestParam(defaultValue = "desc") String order) {
//        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Order.desc(sortBy)));
//        return new ResponseEntity<>(packageService.getAllPackage(pageable), HttpStatus.OK);
//    }

    // @PostMapping("/add")
    // public ResponseEntity<Package> addPackage(@RequestBody Package pack) {
    //     return new ResponseEntity<>(packageService.addPackage(pack), HttpStatus.OK);
    // }
    @PostMapping("/add")
    public ResponseEntity<Package> addPackage(@RequestParam("image") MultipartFile image, @RequestParam("name") String name, @RequestParam("price") int price) throws IOException {
        String originalFilename = image.getOriginalFilename();
        Path filePath = UPLOAD_DIR.resolve(originalFilename);
        Files.copy(image.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
        String imagePath = "/src/main/resources/static/images/package/" + originalFilename;

        Package pack = new Package();
        pack.setName(name);
        pack.setPrice(price);
        pack.setImage(imagePath);
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

    @GetMapping("")
    public ResponseEntity<Page<Package>> findPackagesByNameContaining(@RequestParam(defaultValue = "") String name,
                                                                        @RequestParam(defaultValue = "0") int page,
                                                                        @RequestParam(defaultValue = "10") int size,
                                                                        @RequestParam(defaultValue = "id") String sortBy,
                                                                        @RequestParam(defaultValue = "desc") String order) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Order.desc(sortBy)));
        return new ResponseEntity<>(packageService.findPackagesByNameContaining(name, pageable), HttpStatus.OK);
    }
}
