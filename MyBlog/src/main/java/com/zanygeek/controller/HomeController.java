package com.zanygeek.controller;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttribute;

import com.zanygeek.entity.BlogCategory;
import com.zanygeek.entity.BlogContent;
import com.zanygeek.entity.BlogManager;
import com.zanygeek.entity.ContentTitle;
import com.zanygeek.entity.LoginMember;
import com.zanygeek.repository.BlogCategoryRepository;
import com.zanygeek.repository.BlogContentRepository;
import com.zanygeek.repository.BlogManagerRepository;
import com.zanygeek.repository.MemberRepository;
import com.zanygeek.repository.UploadImageRepository;
import com.zanygeek.service.BlogContentService;
import com.zanygeek.service.LoginService;

@Controller
public class HomeController {
	@Autowired
	BlogManagerRepository blogManagerRepository;
	@Autowired
	BlogCategoryRepository blogCategoryRepository;
	@Autowired
	BlogContentRepository blogContentRepository;
	@Autowired
	MemberRepository memberRepository;
	@Autowired
	BlogContentService blogContentService;
	@Autowired
	UploadImageRepository uploadImageRepository;
	//@Autowired
	//ImageStore imageStore;

	@GetMapping("/")
	public String home() {
		return "redirect:/Blog Name";
	}

	@GetMapping("{blogTitle}")
	public String blog(@PathVariable String blogTitle, @ModelAttribute BlogCategory blogCategory,
			@RequestParam(required = false, defaultValue = "-1") int contentId, Model model,
			HttpServletRequest request) {
		List<ContentTitle> contents = blogContentRepository.findContentTitlesByBlogTitleWithOutLocked(blogTitle);
		if (request.getAttribute("owner") != null && (boolean) request.getAttribute("owner") == true) {
			contents = blogContentRepository.findContentTitlesByBlogTitle(blogTitle);
		}
		model.addAttribute("contents", contents);
		if (!contents.isEmpty()) {
			if (contentId == -1)
				model.addAttribute("content", blogContentService.findFirstContent(contents));
			else
				model.addAttribute("content", blogContentRepository.findById(contentId).get());
		}
		return "blog/home";
	}

	@GetMapping("{blogTitle}/search")
	public String blog(@PathVariable String blogTitle, @ModelAttribute BlogCategory blogCategory, String contentTitle,
			HttpServletRequest request, Model model) {
		List<ContentTitle> contents = blogContentRepository
				.findContentTitlesByBlogTitleAndContentTitleWithOutLocked(blogTitle, contentTitle);
		if (request.getAttribute("owner") != null && (boolean) request.getAttribute("owner") == true) {
			contents = blogContentRepository.findContentTitlesByBlogTitleAndContentTitle(blogTitle, contentTitle);
		}
		model.addAttribute("contents", contents);
		if (!contents.isEmpty())
			model.addAttribute("content", blogContentService.findFirstContent(contents));
		return "blog/home";
	}

	@GetMapping(value = { "{blogTitle}/category/{categoryTitle}/{contentId}", "{blogTitle}/category/{categoryTitle}" })
	public String category(Model model, HttpServletRequest request, @PathVariable String blogTitle,
			@PathVariable String categoryTitle, @PathVariable(required = false) Integer contentId,
			@SessionAttribute(name = LoginService.USER_COOKIE, required = false) LoginMember loginMember,
			@ModelAttribute BlogCategory blogCategory) {

		BlogManager blogManager = blogManagerRepository.findByTitle(blogTitle).get();
		BlogCategory category = blogCategoryRepository.findByBlogManagerIdAndTitle(blogManager.getId(), categoryTitle);
		if (category != null) {
			List<ContentTitle> contents = blogContentRepository
					.findContentTitlesByBlogCategoryWithOutLocked(category.getId());
			if (request.getAttribute("owner") != null && (boolean) request.getAttribute("owner") == true)
				contents = blogContentRepository.findContentTitlesByBlogCategory(category.getId());

			model.addAttribute("category", category);
			model.addAttribute("contents", contents);
			if (contentId != null) {
				BlogContent content = blogContentRepository.findById(contentId).get();
				if (content.getBlogTitle().equals(blogTitle))
					model.addAttribute("content", content);
				else
					return "redirect:/";
			} else if (!contents.isEmpty())
				model.addAttribute("content", blogContentService.findFirstContent(contents));
			return "blog/home";
		} else
			return "redirect:/";

	}

	@PostMapping("{blogTitle}/category/add")
	public String categoryAdd(@PathVariable String blogTitle, @ModelAttribute BlogCategory blogCategory,
			@RequestParam(required = false, defaultValue = "/") String redirectURL) {
		BlogManager blogManager = blogManagerRepository.findByTitle(blogTitle).get();
		blogCategory.setBlogManagerId(blogManager.getId());
		while (blogCategoryRepository.findByBlogManagerIdAndTitle(blogManager.getId(),
				blogCategory.getTitle()) != null) {
			blogCategory.setTitle(blogCategory.getTitle() + "이름 중복");
		}
		blogCategoryRepository.save(blogCategory);
		return "redirect:" + redirectURL;
	}

	@GetMapping("{blogTitle}/category/delete/{categoryId}")
	public String categoryDelete(@PathVariable String blogTitle, @PathVariable int categoryId,
			@ModelAttribute BlogCategory blogCategory, @RequestParam String redirectURL) {
		blogCategoryRepository.deleteById(categoryId);
		return "redirect:" + redirectURL;
	}

	@GetMapping({ "{blogTitle}/content/add", "{blogTitle}/content/edit/{contentId}" })
	public String addContent(@PathVariable String blogTitle,
			@RequestParam(required = false, value = "blogCategoryId", defaultValue = "0") int blogCategoryId,
			@PathVariable(required = false, value = "contentId") Integer contentId, Model model,
			@ModelAttribute BlogContent blogContent, @ModelAttribute BlogCategory blogCategory,
			HttpServletRequest request) throws UnsupportedEncodingException {
		blogTitle = URLEncoder.encode(blogTitle, "UTF-8");
		if (request.getAttribute("owner") == null) {
			return "redirect:/login?redirectURL=" + request.getRequestURI();
		}
		List<ContentTitle> contents = blogContentRepository.findContentTitlesByBlogTitle(blogTitle);

		model.addAttribute("contents", contents);
		model.addAttribute("blogCategoryId", blogCategoryId);
		if (contentId != null) {
			BlogContent content = blogContentRepository.findById(contentId).get();
			model.addAttribute("blogContent", content);
			model.addAttribute("content", content);
			model.addAttribute("blogCategoryId", content.getBlogCategory().getId());
		} else
			model.addAttribute("content", new BlogContent());
		return "blog/editor";
	}

	@PostMapping("{blogTitle}/content/add")
	public String addContent(@PathVariable String blogTitle, BlogContent blogContent,
			@SessionAttribute(name = LoginService.USER_COOKIE, required = true) LoginMember loginMember)
			throws UnsupportedEncodingException {
		BlogContent content = blogContent;
		blogTitle = URLEncoder.encode(blogTitle, "UTF-8");
		blogTitle = blogTitle.replaceAll("[+]", "%20");
		content.setMember(memberRepository.findById(loginMember.getId()).get());
		content.setReportingDate(new Date());
		blogContentRepository.save(blogContent);
		return "redirect:/" + blogTitle;
	}

	@GetMapping("{blogTitle}/content/delete")
	public String deleteContent(@PathVariable String blogTitle, @RequestParam(required = false) String categoryTitle,
			@RequestParam int contentId, HttpServletRequest request) throws UnsupportedEncodingException {
		if ((boolean) request.getAttribute("owner") == true && request.getAttribute("owner") != null) {
			blogContentRepository.deleteById(contentId);
			blogTitle = URLEncoder.encode(blogTitle, "UTF-8");
			blogTitle = blogTitle.replaceAll("[+]", "%20");
			if (categoryTitle == null) {
				return "redirect:/" + blogTitle;
			}
			categoryTitle = URLEncoder.encode(categoryTitle, "UTF-8");
			categoryTitle = categoryTitle.replaceAll("[+]", "%20");
			return "redirect:/" + blogTitle + "/category/" + categoryTitle;
		} else
			return "redirect:/";
	}
	
	@GetMapping("/test/test")
	@ResponseBody
	public Object test(HttpServletRequest request, HttpSession session) {
		System.out.println(session.getAttribute("test"));
		System.out.println(session.getAttribute(LoginService.USER_COOKIE));
		return session.getAttribute("test");
	}
	/*
	@PostMapping("/image")
	@ResponseBody
	public ResponseEntity<?> handleFileUpload(@RequestParam("file") MultipartFile file) {
		try {

			UploadImage uploadImage = imageStore.storeFile(file);
			uploadImageRepository.save(uploadImage);
			return ResponseEntity.ok().body("/images/" + uploadImage.getStoreFileName());
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.badRequest().build();
		}
	}

	@ResponseBody
	@GetMapping("/images/{filename}")
	public Resource downloadImage(@PathVariable String filename) throws MalformedURLException {
		return new UrlResource("file:" + imageStore.getFullPath(filename));
	}
*/
}
