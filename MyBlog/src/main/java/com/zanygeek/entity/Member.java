package com.zanygeek.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotBlank;

import lombok.Data;

@Data
@Entity
public class Member {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	@NotBlank
	private String name;
	@NotBlank
	private String userId;
	@NotBlank
	private String password;
	@NotBlank
	private String nickname;
	private String photo;
	private Date startDate;
	private int blogManagerId;
}
