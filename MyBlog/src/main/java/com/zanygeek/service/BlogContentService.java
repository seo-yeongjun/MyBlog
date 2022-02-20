package com.zanygeek.service;

import com.zanygeek.entity.BlogContent;
import com.zanygeek.repository.BlogContentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public final class BlogContentService {
	@Autowired
	BlogContentRepository blogContentRepository;

	public BlogContent findFirstContent(List<BlogContent> contents) {
		return blogContentRepository.findById(contents.get(0).getId()).get();
	}

	public void viewPlus(BlogContent content) {
		content.setSee(content.getSee()+1);
		blogContentRepository.save(content);
	}
}
