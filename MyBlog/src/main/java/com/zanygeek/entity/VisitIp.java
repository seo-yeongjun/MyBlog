package com.zanygeek.entity;

import lombok.Data;
import org.springframework.lang.Nullable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Data
@Entity
public class VisitIp {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	private String ip;
	@Nullable
	private String title;
	@Nullable
	private Integer contentId;

	public VisitIp() {

	}

	public VisitIp(String ip, String title) {
		this.ip = ip;
		this.title = title;
	}

	public VisitIp(String ip, int contentId) {
		this.ip = ip;
		this.contentId = contentId;
	}
}
