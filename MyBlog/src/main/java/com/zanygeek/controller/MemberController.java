package com.zanygeek.controller;

import com.zanygeek.form.JoinForm;
import com.zanygeek.repository.BlogManagerRepository;
import com.zanygeek.repository.MemberRepository;
import com.zanygeek.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

@Controller
@RequestMapping("member")
public class MemberController {
	@Autowired
	MemberRepository memberRepository;
	@Autowired
	MemberService memberService;
	@Autowired
	BlogManagerRepository blogManagerRepository;

	@GetMapping("join")
	public String join(JoinForm joinForm) {
		return "member/join";
	}

	@PostMapping("join")
	public String join(Model model,@Validated @ModelAttribute JoinForm joinForm, BindingResult bindingResult) throws UnsupportedEncodingException {
		if (bindingResult.hasErrors()) {
			model.addAttribute(joinForm);
			return "member/join";
		}
		if(memberService.error(joinForm, bindingResult)) {
			return "member/join";
		}
		
		String blogTitle = memberService.save(joinForm);
		blogTitle = URLEncoder.encode(blogTitle, "UTF-8");
		blogTitle =blogTitle.replaceAll("[+]", "%20");
		return "redirect:/"+blogTitle;
	}
}
