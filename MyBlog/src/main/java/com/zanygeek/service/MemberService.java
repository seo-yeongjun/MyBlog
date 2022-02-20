package com.zanygeek.service;

import com.zanygeek.entity.Member;
import com.zanygeek.form.JoinForm;
import com.zanygeek.repository.BlogManagerRepository;
import com.zanygeek.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;


@Service
public class MemberService {
	@Autowired
	MemberRepository memberRepository;
	@Autowired
	BlogManagerRepository blogManagerRepository;
	@Autowired
	PasswordEncoder passwordEncoder;

	public Boolean error(JoinForm joinForm, BindingResult bindingResult) {
		if (!memberRepository.findByUserId(joinForm.getMember().getUserId()).isEmpty()) {
			bindingResult.addError(new FieldError("joinForm", "userId", "이미 존재하는 아이디 입니다."));
		}
		if (!(joinForm.getMember().getPassword().equals(joinForm.getPassword2()))) {
			bindingResult.addError(new FieldError("joinForm", "password2", "비밀번호가 틀립니다."));
		}
		if(!blogManagerRepository.findByTitle(joinForm.getTitle()).isEmpty()) {
			bindingResult.addError(new FieldError("joinForm", "title", "이미 존재하는 블로그 이름입니다."));
		}
		return bindingResult.hasErrors();
	}

	public String save(JoinForm joinForm) {
		blogManagerRepository.save(joinForm.getBlogManager());
		Member member =joinForm.getMember();
		String blogTitle = joinForm.getBlogManager().getTitle();
		String encPassword = passwordEncoder.encode(member.getPassword());
		member.setPassword(encPassword);
		member.setBlogManagerId(blogManagerRepository.findByTitle(blogTitle).get().getId());
		memberRepository.save(member);
		return blogTitle;
	}
}
