package com.zanygeek.service;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.zanygeek.repository.VisitIpRepository;

@Service
@EnableScheduling
public final class BlogVisitService {
@Autowired
VisitIpRepository visitIpRepository;

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
	
	@Scheduled(fixedDelay = 5000)
	//cron="0 0 00 * * ?"
	public void reSet() {
		visitIpRepository.deleteAll();
		visitIpRepository.resetToday();
		System.out.println("초기화");
	}
}
