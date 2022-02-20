package com.zanygeek.service;

import com.zanygeek.repository.VisitIpRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;

@Service
@EnableScheduling
public final class BlogVisitService {
@Autowired
VisitIpRepository visitIpRepository;
/*
	생략
	*/
	public String getIp(HttpServletRequest request) {

		String ip = request.getHeader("X-Forwarded-For");

		if (ip == null) {
			ip = request.getHeader("Proxy-Client-IP");
		}
		if (ip == null) {
			ip = request.getHeader("WL-Proxy-Client-IP");
		}
		if (ip == null) {
			ip = request.getHeader("HTTP_CLIENT_IP");
		}
		if (ip == null) {
			ip = request.getHeader("HTTP_X_FORWARDED_FOR");
		}
		if (ip == null) {
			ip = request.getRemoteAddr();
		}
		return ip;

	}

	//일일 방문 아이피 삭제, 일일 방문자 수 0으로
	@Scheduled(cron = "0 0 0 * * ?")
	public void reSet() {
		visitIpRepository.deleteAll();
		visitIpRepository.resetToday();
	}
}
