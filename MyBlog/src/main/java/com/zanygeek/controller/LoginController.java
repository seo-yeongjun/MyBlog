package com.zanygeek.controller;

import com.zanygeek.entity.LoginMember;
import com.zanygeek.form.LoginForm;
import com.zanygeek.repository.BlogManagerRepository;
import com.zanygeek.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

@Controller
public class LoginController {
	@Autowired
	LoginService loginService;
	@Autowired
	BlogManagerRepository blogManagerRepository;

	@GetMapping("login")
	public String login(@ModelAttribute LoginForm loginForm) {
		return "member/loginForm";
	}

	@PostMapping("login")
	public String login(@Validated @ModelAttribute LoginForm loginForm, BindingResult bindingResult,
			HttpServletRequest request) throws UnsupportedEncodingException {
		LoginMember loginMember = loginService.login(loginForm, bindingResult);
		if (loginMember == null) {
			return "member/loginForm";
		}
		HttpSession session = request.getSession();
		session.setAttribute(LoginService.USER_COOKIE, loginMember);
		String title = blogManagerRepository.findById(loginMember.getBlogManagerId()).get().getTitle();
		title = URLEncoder.encode(title, "UTF-8");
		title =title.replaceAll("[+]", "%20");
		return "redirect:/" + title;
	}

	@GetMapping("logout")
	public String logout(HttpServletRequest request, @RequestParam String redirectURL) {
		HttpSession session = request.getSession(false);
		if(session!=null) {
		session.removeAttribute(LoginService.USER_COOKIE);}
		redirectURL = redirectURL.replaceAll("[+]","%20");
		return "redirect:" + redirectURL;
	}
}
