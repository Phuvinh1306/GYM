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
import java.security.SecureRandom;
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
    public ResponseEntity<Package> addPackage(@RequestParam(value = "image", required = false) MultipartFile image, @RequestParam("name") String name, @RequestParam("price") int price) throws IOException {
        String imagePath;
        if (image == null || image.isEmpty()) {
            imagePath = "/src/main/resources/static/images/package/default.png";
        } else {
            String originalFilename = image.getOriginalFilename();
            String extension = originalFilename.substring(originalFilename.lastIndexOf('.'));
            String newFilename = System.currentTimeMillis() + "_" + new SecureRandom().nextInt() + extension;
            Path filePath = UPLOAD_DIR.resolve(newFilename);
            Files.copy(image.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
            imagePath = "/src/main/resources/static/images/package/" + newFilename;
        }

        Package pack = new Package();
        pack.setName(name);
        pack.setPrice(price);
        pack.setImage(imagePath);
        return new ResponseEntity<>(packageService.addPackage(pack), HttpStatus.OK);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Package> updatePackage(@RequestParam("image") MultipartFile image, @RequestParam("name") String name, @RequestParam("price") int price, @PathVariable Long id) throws IOException{
        String imagePath;
        if (image.isEmpty()) {
            imagePath = "/src/main/resources/static/images/package/default.jpg";
        } else {
            String originalFilename = image.getOriginalFilename();
            String extension = originalFilename.substring(originalFilename.lastIndexOf('.'));
            String newFilename = System.currentTimeMillis() + "_" + new SecureRandom().nextInt() + extension;
            Path filePath = UPLOAD_DIR.resolve(newFilename);
            Files.copy(image.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
            imagePath = "/src/main/resources/static/images/package/" + newFilename;
        }

        Package pack = new Package();
        pack.setName(name);
        pack.setPrice(price);
        pack.setImage(imagePath);
        return new ResponseEntity<>(packageService.updatePackage(pack, id), HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deletePackage(@PathVariable("id") Long id) {
        packageService.deletePackage(id);
        return new ResponseEntity<>(new ResponseMessage("deleted"), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Package> getPackage(@PathVariable("id") Long id) {
        Package pack = packageService.getPackage(id);
        String imagePath = System.getProperty("user.dir") + pack.getImage();
        imagePath = imagePath.replace("\\", "/");
        pack.setImage(imagePath);
        System.out.println(pack.getImage());
        return new ResponseEntity<>(pack, HttpStatus.OK);
    }

    @GetMapping("")
    public ResponseEntity<Page<Package>> findPackagesByNameContaining(@RequestParam(defaultValue = "") String name,
                                                                        @RequestParam(defaultValue = "0") int page,
                                                                        @RequestParam(defaultValue = "10") int size,
                                                                        @RequestParam(defaultValue = "id") String sortBy,
                                                                        @RequestParam(defaultValue = "desc") String order) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Order.desc(sortBy)));
        Page<Package> packages = packageService.findPackagesByNameContaining(name, pageable);
        packages.forEach(pack -> {
            String imagePath = System.getProperty("user.dir") + pack.getImage();
            imagePath = imagePath.replace("\\", "/");
            pack.setImage(imagePath);
        });
        return new ResponseEntity<>(packages, HttpStatus.OK);
    }
}
