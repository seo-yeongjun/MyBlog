package com.zanygeek.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import com.zanygeek.form.JoinForm;
import com.zanygeek.repository.BlogManagerRepository;
import com.zanygeek.repository.MemberRepository;

@Service
public class MemberService {
	@Autowired
	MemberRepository memberRepository;
	@Autowired
	BlogManagerRepository blogManagerRepository;

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

}
