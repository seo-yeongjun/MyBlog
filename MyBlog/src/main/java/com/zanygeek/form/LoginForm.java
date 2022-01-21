package com.zanygeek.form;

import javax.validation.constraints.NotBlank;

import lombok.Data;

@Data
public class LoginForm {

	@NotBlank
	private String userId;
	@NotBlank
	private String password;
	
}
