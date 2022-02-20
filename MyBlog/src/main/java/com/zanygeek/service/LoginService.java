package com.zanygeek.service;

import com.zanygeek.entity.LoginMember;
import com.zanygeek.entity.Member;
import com.zanygeek.form.LoginForm;
import com.zanygeek.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.util.Optional;


@Service
public final class LoginService {
	@Autowired
	MemberRepository memberRepository;
	@Autowired
	PasswordEncoder passwordEncoder;

	public static final String USER_COOKIE = "userCookie";

	public LoginMember login(LoginForm form, BindingResult result) {
		Optional<Member> member = memberRepository.findByUserId(form.getUserId());
		LoginMember loginMember = new LoginMember();
		if (member.isEmpty()) {
			result.addError(new FieldError("joinForm", "userId", "일치하는 아이디가 없습니다."));
			return null;
		}
		
		if (passwordEncoder.matches(form.getPassword(), member.get().getPassword())) {
			loginMember.setId(member.get().getId());
			loginMember.setBlogManagerId(member.get().getBlogManagerId());
			return loginMember;
		} else
			result.addError(new FieldError("joinForm", "password", "비밀번호가 틀렸습니다."));
			return null;
	}
}
