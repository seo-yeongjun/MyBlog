package com.zanygeek.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.zanygeek.entity.LoginMember;
import com.zanygeek.form.LoginForm;
import com.zanygeek.service.LoginService;

@Controller
public class LoginController {
	@Autowired
	LoginService loginService;

	@GetMapping("login")
	public String login(@ModelAttribute LoginForm loginForm){
		return "member/loginForm";
	}
	@PostMapping("login")
	public String login(@Validated @ModelAttribute LoginForm loginForm, BindingResult bindingResult, HttpServletRequest request) {
		LoginMember loginMember = loginService.login(loginForm, bindingResult);
		if(loginMember ==null) {
			return "member/loginForm";
		}
		HttpSession session = request.getSession();
		session.setAttribute(LoginService.USER_COOKIE, loginMember);
		return "redirect:/";
	}
	
	@GetMapping("logout")
	public String logout(HttpServletRequest request) {
		HttpSession session = request.getSession(false);
		session.removeAttribute(LoginService.USER_COOKIE);
		return "redirect:/";
	}
}
