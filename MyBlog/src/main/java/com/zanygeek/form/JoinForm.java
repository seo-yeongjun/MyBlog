package com.zanygeek.form;

import java.util.Date;

import javax.validation.constraints.NotBlank;

import com.zanygeek.entity.BlogManager;
import com.zanygeek.entity.Member;

import lombok.Data;

@Data
public class JoinForm {

	@NotBlank
	private String name;
	@NotBlank
	private String userId;
	@NotBlank
	private String password;
	@NotBlank
	private String password2;
	@NotBlank
	private String nickname;
	private String photo;
	private Date startDate;
	private int blogManagerId;
	@NotBlank
	private String title;
	
	public Member getMember() {
		Member member = new Member();
		member.setName(name);
		member.setNickname(nickname);
		member.setPassword(password);
		member.setStartDate(new Date());
		member.setUserId(userId);
		
		return member;
	}
	
	public BlogManager getBlogManager() {
		BlogManager blogManager = new BlogManager();
		blogManager.setTitle(title);
		return blogManager;
	}
}
