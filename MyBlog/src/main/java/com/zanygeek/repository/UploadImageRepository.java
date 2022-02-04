package com.zanygeek.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.zanygeek.entity.UploadImage;

@Repository
public interface UploadImageRepository extends JpaRepository<UploadImage, Integer> {

}
