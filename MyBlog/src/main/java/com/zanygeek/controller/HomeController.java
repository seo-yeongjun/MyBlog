package com.zanygeek.controller;

import com.zanygeek.entity.*;
import com.zanygeek.repository.*;
import com.zanygeek.service.BlogContentService;
import com.zanygeek.service.FileStore;
import com.zanygeek.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URLEncoder;
import java.util.Date;
import java.util.List;

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
    ThumbnailRepository thumbnailRepository;
    @Autowired
    UploadFileRepository uploadFileRepository;
    @Autowired
    FileStore fileStore;
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
        List<BlogContent> contents = blogContentRepository.findAllByBlogTitleAndLockedIsOrderByReportingDateDesc(blogTitle, false);
        if (request.getAttribute("owner") != null && (boolean) request.getAttribute("owner") == true) {
            contents = blogContentRepository.findAllByBlogTitleOrderByReportingDateDesc(blogTitle);
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
        List<BlogContent> contents = blogContentRepository
                .findAllByBlogTitleAndTitleAndLockedIsOrderByReportingDateDesc(blogTitle, contentTitle, false);
        if (request.getAttribute("owner") != null && (boolean) request.getAttribute("owner") == true) {
            contents = blogContentRepository.findAllByBlogTitleAndTitleOrderByReportingDateDesc(blogTitle, contentTitle);
        }
        model.addAttribute("contents", contents);
        if (!contents.isEmpty())
            model.addAttribute("content", blogContentService.findFirstContent(contents));
        return "blog/home";
    }

    @GetMapping(value = {"{blogTitle}/category/{categoryTitle}/{contentId}", "{blogTitle}/category/{categoryTitle}"})
    public String category(Model model, HttpServletRequest request, @PathVariable String blogTitle,
                           @PathVariable String categoryTitle, @PathVariable(required = false) Integer contentId,
                           @SessionAttribute(name = LoginService.USER_COOKIE, required = false) LoginMember loginMember,
                           @ModelAttribute BlogCategory blogCategory) {

        BlogManager blogManager = blogManagerRepository.findByTitle(blogTitle).get();
        BlogCategory category = blogCategoryRepository.findByBlogManagerIdAndTitle(blogManager.getId(), categoryTitle);
        if (category != null) {
            List<BlogContent> contents = blogContentRepository
                    .findAllByBlogCategoryIdAndLockedOrderByReportingDateDesc(category.getId(), false);
            if (request.getAttribute("owner") != null && (boolean) request.getAttribute("owner") == true)
                contents = blogContentRepository.findAllByBlogCategoryIdOrderByReportingDateDesc(category.getId());

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

    @GetMapping({"{blogTitle}/content/add", "{blogTitle}/content/edit/{contentId}"})
    public String addContent(@PathVariable String blogTitle,
                             @RequestParam(required = false, value = "blogCategoryId", defaultValue = "0") int blogCategoryId,
                             @PathVariable(required = false, value = "contentId") Integer contentId, Model model,
                             @ModelAttribute BlogContent blogContent, @ModelAttribute BlogCategory blogCategory,
                             @ModelAttribute List<MultipartFile> uploadFiles1, @ModelAttribute MultipartFile thumbnail1,
                             HttpServletRequest request) throws UnsupportedEncodingException {
        blogTitle = URLEncoder.encode(blogTitle, "UTF-8");
        if (request.getAttribute("owner") == null) {
            return "redirect:/login?redirectURL=" + request.getRequestURI();
        }
        List<BlogContent> contents = blogContentRepository.findAllByBlogTitleOrderByReportingDateDesc(blogTitle);

        model.addAttribute("contents", contents);
        model.addAttribute("blogCategoryId", blogCategoryId);
        if (contentId != null) {
            BlogContent content = blogContentRepository.findById(contentId).get();
            model.addAttribute("blogContent", content);
            model.addAttribute("content", content);
            model.addAttribute("blogCategoryId", content.getBlogCategory().getId());
            model.addAttribute("thumbnail1", content.getThumbnail());
            model.addAttribute("uploadFiles1", content.getUploadFiles());
        } else
            model.addAttribute("content", new BlogContent());
        return "blog/editor";
    }

    @PostMapping("{blogTitle}/content/add")
    public String addContent(@PathVariable String blogTitle, BlogContent blogContent, @ModelAttribute List<MultipartFile> uploadFiles1,
                             @ModelAttribute MultipartFile thumbnail1, @SessionAttribute(name = LoginService.USER_COOKIE, required = true) LoginMember loginMember)
            throws IOException {
        BlogContent content = blogContent;
        blogTitle = URLEncoder.encode(blogTitle, "UTF-8");
        blogTitle = blogTitle.replaceAll("[+]", "%20");
        content.setMember(memberRepository.findById(loginMember.getId()).get());
        content.setReportingDate(new Date());
        content = blogContentRepository.save(blogContent);
        int id = content.getId();
        List<UploadFile> uploadFiles = fileStore.storeFiles(uploadFiles1, id);
        Thumbnail thumbnail = fileStore.storeThumbnail(thumbnail1, id);
        uploadFileRepository.saveAll(uploadFiles);
        if (thumbnail != null)
            thumbnailRepository.save(thumbnail);
        return "redirect:/" + blogTitle;
    }

    @GetMapping("{blogTitle}/content/delete")
    public String deleteContent(@PathVariable String blogTitle, @RequestParam(required = false) String categoryTitle,
                                @RequestParam int contentId, HttpServletRequest request) throws UnsupportedEncodingException {
        if ((boolean) request.getAttribute("owner") && request.getAttribute("owner") != null) {
            thumbnailRepository.deleteAllByTblogContentId(contentId);
            uploadFileRepository.deleteAllByBlogContentId(contentId);
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

    @ResponseBody
    @GetMapping(value = "/thumbnail/{thumbnail}", produces = MediaType.IMAGE_JPEG_VALUE)
    public Resource getImageWithMediaType(@PathVariable String thumbnail) throws MalformedURLException {
        return new UrlResource("file:" + fileStore.getFullPath(thumbnail));
    }
/*
	@ResponseBody
	@GetMapping("/images/{filename}")
	public Resource downloadImage(@PathVariable String filename) throws MalformedURLException {
		return new UrlResource("file:" + imageStore.getFullPath(filename));
	}
*/
}
