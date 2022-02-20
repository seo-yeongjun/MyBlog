package com.zanygeek.interceptor;

import com.zanygeek.entity.BlogManager;
import com.zanygeek.entity.LoginMember;
import com.zanygeek.repository.BlogManagerRepository;
import com.zanygeek.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.HandlerMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Map;

@Component
public class OwnerInterceptor implements HandlerInterceptor {
	@Autowired
	BlogManagerRepository blogManagerRepository;

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		String requestURI = request.getRequestURI();
		HttpSession session = request.getSession(false);

		Map<?, ?> pathVariables = (Map<?, ?>) request.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE);
		String path = (String) pathVariables.get("blogTitle");
		if (blogManagerRepository.findByTitle(path).isEmpty()) {
			response.sendRedirect("/");
			return false;
		}
		BlogManager blogManager = blogManagerRepository.findByTitle(path).get();
		if (session == null || session.getAttribute(LoginService.USER_COOKIE) == null) {
			response.sendRedirect("/login?redirectURL=" + requestURI);
			return false;
		}
		LoginMember loginMember = (LoginMember) session.getAttribute(LoginService.USER_COOKIE);
		if (loginMember.getBlogManagerId() != blogManager.getId()) {
			request.setAttribute("login", true);
			response.sendRedirect("/");
			return false;
		}
		request.setAttribute("owner", true);
		request.setAttribute("login", true);
		return true;
	}
}
