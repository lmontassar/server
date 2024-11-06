package com.backend.server.controller;

import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.backend.server.entity.Auction;
import com.backend.server.entity.Images;
import com.backend.server.service.AuctionService;
import com.backend.server.service.ImagesService;

import io.jsonwebtoken.io.IOException;

import java.nio.file.Path;

@RestController
@RequestMapping(path="/images")
public class ImagesController {
    private final String UPLOAD_DIR = "upload/auctions/";

    @Autowired
    ImagesService imagesService;
    @Autowired
    AuctionService auctionService;

    @GetMapping("/all")
    public List<Images> getAllImages() {
        return imagesService.findAll();
    }
    @GetMapping("/auction/{id}")
    public ResponseEntity<?> getImageById(@PathVariable Long id) {
        Auction a = auctionService.getAuction(id);
        if (a == null) return ResponseEntity.notFound().build();
        try{
            List<Images> l = imagesService.findByAuction(a);
            return ResponseEntity.accepted().body(l);
        }catch(Exception e){
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getImageByAuction(@PathVariable Long id) {
        Images i = imagesService.findById(id);
        if (i == null) return ResponseEntity.notFound().build();
        else return ResponseEntity.accepted().body(i);
    }

    @GetMapping("/upload/auction/{imageName:.+}")
    public ResponseEntity<?> serveFile(@PathVariable String imageName) throws MalformedURLException {
        Path file = Paths.get("upload/auctions/" + imageName);
        Resource resource = new UrlResource(file.toUri());
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }

    @PostMapping("/add")
    public ResponseEntity<Images> createImage(@RequestParam("file") MultipartFile file,
                                               @RequestParam("auctionId") Long auctionId) {
        try {
            String imageUrl = uploadFile(file); 
            Images newImage = new Images();
            newImage.setUrl(imageUrl);
            newImage.setAuction(auctionService.getAuction(auctionId)); 
            Images createdImage = imagesService.create(newImage);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdImage);

        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    private String uploadFile(MultipartFile file) throws IOException {

        Path uploadPath = Paths.get(UPLOAD_DIR);
        try{ 
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }
        } catch(Exception e){

        }

        String originalFilename = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));
        String extension = originalFilename.substring(originalFilename.lastIndexOf("."));
        String fileName = "auction-" + System.currentTimeMillis() + extension;

        Path filePath = uploadPath.resolve(fileName);
        try{
            Files.copy(file.getInputStream(), filePath);
        } catch(Exception e){
        }
        return fileName.toString();
    }
    
}
