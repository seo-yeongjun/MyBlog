package com.zanygeek.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.Data;

@Data
@Entity
public class BlogContent {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	private int blogCategoryId;
	private String text;
	private String title;
	private int see;
	private int memberId;
	private boolean locked;
}
