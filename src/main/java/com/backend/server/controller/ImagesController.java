package com.backend.server.controller;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.backend.server.entity.Images;
import com.backend.server.service.ImagesService;

import io.jsonwebtoken.io.IOException;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@RestController
@RequestMapping(path="/images")
public class ImagesController {
    private final String UPLOAD_DIR = "uploads/auctions/";

    @Autowired
    ImagesService imagesService;

    @GetMapping("/all")
    public List<Images> getAllImages() {
        return imagesService.findAll();
    }
    @GetMapping("/{id}")
    public ResponseEntity<Images> getImageById(@PathVariable Long id) {
        Images i = imagesService.findById(id);
        if (i == null) return ResponseEntity.notFound().build();
        else return ResponseEntity.accepted().body(i);
    }
    @PostMapping("/add")
    public ResponseEntity<Images> createImage(@RequestParam("file") MultipartFile file,
                                               @RequestParam("auctionId") Long auctionId) {
        try {
            String imageUrl = uploadFile(file); 

            Images newImage = new Images();
            newImage.setUrl(imageUrl);
            newImage.setAuction(imagesService.findAuctionById(auctionId)); 

            Images createdImage = imagesService.create(newImage);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdImage);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    private String uploadFile(MultipartFile file) throws IOException {
        // Create the upload directory if it doesn't exist
        Path uploadPath = Paths.get(UPLOAD_DIR);
        try{ 
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }
        } catch(Exception e){

        }

        // Define the file path
        String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
        Path filePath = uploadPath.resolve(fileName);
        
        // Save the file to the specified location
        try{
            Files.copy(file.getInputStream(), filePath);
        } catch(Exception e){

        }

        // Return the URL or path of the uploaded file
        return filePath.toString(); // Adjust this to return a proper URL if needed
    }
    
}
