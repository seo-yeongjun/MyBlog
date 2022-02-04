package com.zanygeek.interceptor;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.HandlerMapping;
import org.thymeleaf.util.MapUtils;

import com.zanygeek.entity.BlogManager;
import com.zanygeek.entity.LoginMember;
import com.zanygeek.repository.BlogCategoryRepository;
import com.zanygeek.repository.BlogManagerRepository;
import com.zanygeek.service.LoginService;

@Component
public class BlogInterceptor implements HandlerInterceptor {
	@Autowired
	BlogManagerRepository blogManagerRepository;
	@Autowired
	BlogCategoryRepository blogCategoryRepository;
	

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		HttpSession session = request.getSession(false);
		Map<?, ?> pathVariables = (Map<?, ?>) request.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE);
		if (!MapUtils.isEmpty(pathVariables)) {
			String path = (String) pathVariables.get("blogTitle");
			if (blogManagerRepository.findByTitle(path).isEmpty()) {
				response.sendRedirect("/");
				return false;
			}
		} else {
			return true;
		}
		String path = (String) pathVariables.get("blogTitle");
		if (session == null || session.getAttribute(LoginService.USER_COOKIE) == null) {
			request.setAttribute("owner", false);
		} else {
			LoginMember loginMember = (LoginMember) session.getAttribute(LoginService.USER_COOKIE);
			request.setAttribute("login", true);
			BlogManager blogManager = blogManagerRepository.findById(loginMember.getBlogManagerId()).get();
			if (path.equals(blogManager.getTitle()))
				request.setAttribute("owner", true);
			else
				request.setAttribute("owner", false);
		}
		request.setAttribute("blogTitle", path);
		request.setAttribute("blogCategories",
				blogCategoryRepository.findByBlogManagerId(blogManagerRepository.findByTitle(path).get().getId()));
		BlogManager blogManager = blogManagerRepository.findByTitle(path).get();
		request.setAttribute("blogManager", blogManager);
		return true;
	}

}
