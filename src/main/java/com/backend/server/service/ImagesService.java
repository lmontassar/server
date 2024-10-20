package com.backend.server.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.backend.server.entity.Auction;
import com.backend.server.entity.Images;
import com.backend.server.repository.ImagesRepo;

@Service
public class ImagesService {
    @Autowired
    private ImagesRepo imagesRepo;

    // Find all images
    public List<Images> findAll() {
        return imagesRepo.findAll();
    }

    public List<Images> findByAuction(Auction a){
        return imagesRepo.findAllByAuction(a);
    }

    // Find an image by ID
    public Images findById(Long id) {
        return imagesRepo.findById(id).orElse(null);
    }

    // Create a new image
    public Images create(Images image) {
        return imagesRepo.save(image);
    }

    // Update an existing image
    public Images update(Long id, Images imageDetails) {
        Images image = imagesRepo.findById(id).orElse(null);

        if(image != null){
            image.setUrl(imageDetails.getUrl());
            imagesRepo.save(image);
        }
        return image;
    }

    // Delete an image by ID
    public void delete(Long id) {
        Images image = imagesRepo.findById(id).orElse(null);
        if(image != null)
        imagesRepo.delete(image);
    }
}
