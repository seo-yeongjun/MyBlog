package com.zanygeek.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.Data;

@Data
@Entity
public class UploadImage {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	 private String uploadFileName;
     private String storeFileName;
     
     public UploadImage(String uploadFileName, String storeFileName) {
         this.uploadFileName = uploadFileName;
         this.storeFileName = storeFileName;
} }
