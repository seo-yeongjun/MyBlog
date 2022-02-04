package com.zanygeek.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.Data;

@Data
@Entity
public class BlogContent {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	@OneToOne
	@JoinColumn(name = "blogCategoryId")
	private BlogCategory blogCategory;
	private String text;
	private String title;
	private int see;
	private String blogTitle;
	private boolean locked;
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date reportingDate;
	
	@OneToOne
	@JoinColumn(name = "memberId")
	private Member member;
}
