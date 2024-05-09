package com.hotrodoan.controller;

import com.hotrodoan.dto.response.ResponseImage;
import com.hotrodoan.model.Image;
import com.hotrodoan.service.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
public class ImageController {
    @Autowired
    private ImageService imageService;

    @PostMapping("/upload")
    public ResponseImage uploadImage(@RequestParam("file")MultipartFile file) throws Exception {
        String downloadUrl = "";
        Image image = imageService.saveImage(file);
        downloadUrl = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/download/")
                .path(image.getId())
                .toUriString();
        return new ResponseImage(image.getFileName(),
                downloadUrl,
                file.getContentType(),
                file.getSize());
    }

    @GetMapping("/download/{imageId}")
    public ResponseEntity<Resource> downloadImage(@PathVariable String imageId) throws Exception {
        Image image = imageService.getImage(imageId);
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(image.getFileType()))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + image.getFileName() + "\"")
                .body(new ByteArrayResource(image.getData()));
    }
}
