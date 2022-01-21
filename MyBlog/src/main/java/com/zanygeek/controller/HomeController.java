package com.zanygeek.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.SessionAttribute;

import com.zanygeek.entity.BlogCategory;
import com.zanygeek.entity.BlogContent;
import com.zanygeek.entity.BlogManager;
import com.zanygeek.entity.LoginMember;
import com.zanygeek.repository.BlogCategoryRepository;
import com.zanygeek.repository.BlogContentRepository;
import com.zanygeek.repository.BlogManagerRepository;
import com.zanygeek.service.LoginService;

@Controller
public class HomeController {
	@Autowired
	BlogManagerRepository blogManagerRepository;
	@Autowired
	BlogCategoryRepository blogCategoryRepository;
	@Autowired
	BlogContentRepository blogContentRepository;

	@GetMapping("/")
	public String home() {
		return "blog/index";
	}

	@GetMapping("/{blogTitle}")
	public String blog(@PathVariable String blogTitle, Model model,
			@SessionAttribute(name = LoginService.USER_COOKIE, required = false) LoginMember loginMember,
			BlogCategory blogCategory) {
		if (blogManagerRepository.findByTitle(blogTitle).isEmpty()) {
			return "redirect:/";
		}
		model.addAttribute("blogCategories",
				blogCategoryRepository.findByBlogManagerId(blogManagerRepository.findByTitle(blogTitle).get().getId()));
		model.addAttribute("blogTitle", blogTitle);
		if (loginMember != null) {
			model.addAttribute("login", true);
			BlogManager blogManager = blogManagerRepository.findById(loginMember.getBlogManagerId()).get();
			if (blogTitle.equals(blogManager.getTitle())) {
				model.addAttribute("owner", true);
				return "blog/home";
			}
		}
		return "blog/home";
	}

	@PostMapping("{blogTitle}/category/add")
	public String categoryAdd(@PathVariable String blogTitle, @ModelAttribute BlogCategory blogCategory) {
		BlogManager blogManager = blogManagerRepository.findByTitle(blogTitle).get();
		blogCategory.setBlogManagerId(blogManager.getId());
		blogCategoryRepository.save(blogCategory);
		return "redirect:/";
	}

	@GetMapping("{blogTitle}/content/add")
	public String addContent(@PathVariable String blogTitle, Model model, BlogCategory blogCategory,
			BlogContent blogContent) {
		model.addAttribute("blogCategories",
				blogCategoryRepository.findByBlogManagerId(blogManagerRepository.findByTitle(blogTitle).get().getId()));
		model.addAttribute("blogTitle", blogTitle);
		return "blog/editor";
	}

	@PostMapping("{blogTitle}/content/add")
	public String addContent(@PathVariable String blogTitle, BlogContent blogContent,
			@SessionAttribute(name = LoginService.USER_COOKIE, required = true) LoginMember loginMember) {
		BlogContent content = blogContent;
		content.setMemberId(loginMember.getId());
		System.out.println(content);
		blogContentRepository.save(content);
		return "redirect:/" + blogTitle + "/content/add";
	}
}
