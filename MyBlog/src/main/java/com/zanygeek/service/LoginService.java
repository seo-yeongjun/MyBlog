package com.zanygeek.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import com.zanygeek.entity.LoginMember;
import com.zanygeek.entity.Member;
import com.zanygeek.form.LoginForm;
import com.zanygeek.repository.MemberRepository;


@Service
public final class LoginService {
	@Autowired
	MemberRepository memberRepository;

	public static final String USER_COOKIE = "userCookie";

	public LoginMember login(LoginForm form, BindingResult result) {
		Optional<Member> member = memberRepository.findByUserId(form.getUserId());
		LoginMember loginMember = new LoginMember();
		if (member.isEmpty()) {
			result.addError(new FieldError("joinForm", "userId", "일치하는 아이디가 없습니다."));
			return null;
		}
		if (member.get().getPassword().equals(form.getPassword())) {
			loginMember.setId(member.get().getId());
			loginMember.setBlogManagerId(member.get().getBlogManagerId());
			return loginMember;
		} else
			return null;
	}
}
