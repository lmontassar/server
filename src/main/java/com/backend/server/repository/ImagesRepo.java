package com.backend.server.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.backend.server.entity.Images;

@Repository
public interface ImagesRepo extends JpaRepository<Images,Long> {
    
}
