package com.zanygeek.interceptor;

import com.zanygeek.entity.BlogContent;
import com.zanygeek.entity.BlogManager;
import com.zanygeek.entity.VisitIp;
import com.zanygeek.repository.BlogManagerRepository;
import com.zanygeek.repository.VisitIpRepository;
import com.zanygeek.service.BlogContentService;
import com.zanygeek.service.BlogVisitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.ui.ModelMap;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class BlogVisitInterceptor implements HandlerInterceptor {

	@Autowired
	BlogVisitService blogVisitService;
	@Autowired
	BlogContentService blogContentService;
	@Autowired
	VisitIpRepository visitIpRepository;
	@Autowired
	BlogManagerRepository blogManagerRepository;

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		return true;
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		String ip = blogVisitService.getIp(request);
		String title = (String) request.getAttribute("blogTitle");
		BlogManager blogManager = blogManagerRepository.findByTitle(title).get();

		if (modelAndView != null) {
			ModelMap model = modelAndView.getModelMap();
			BlogContent content = (BlogContent) model.get("content");
			if (content != null && visitIpRepository.findByIpAndContentId(ip, content.getId()) == null) {
				blogContentService.viewPlus((BlogContent) content);
				visitIpRepository.save(new VisitIp(ip, content.getId()));
			}
			if (visitIpRepository.findByIpAndTitle(ip, title) == null) {
				blogManager.setVisitToday(blogManager.getVisitToday() + 1);
				blogManager.setVisitTotal(blogManager.getVisitTotal() + 1);
				blogManagerRepository.save(blogManager);
				visitIpRepository.save(new VisitIp(ip, title));
			}

			model.addAttribute("blogManager", blogManager);
		}
	}
}
