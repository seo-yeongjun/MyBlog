package com.zanygeek.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zanygeek.entity.BlogContent;
import com.zanygeek.entity.ContentTitle;
import com.zanygeek.repository.BlogContentRepository;

@Service
public final class BlogContentService {
	@Autowired
	BlogContentRepository blogContentRepository;

	public BlogContent findFirstContent(List<ContentTitle> contents) {
		return blogContentRepository.findById(contents.get(0).getId()).get();
	}

	public void viewPlus(BlogContent content) {
		content.setSee(content.getSee()+1);
		blogContentRepository.save(content);
	}
}
