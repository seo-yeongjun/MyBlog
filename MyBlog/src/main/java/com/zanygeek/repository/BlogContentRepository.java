package com.zanygeek.repository;

import com.zanygeek.entity.BlogContent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BlogContentRepository extends JpaRepository<BlogContent, Integer> {


	List<BlogContent> findAllByBlogCategoryIdOrderByReportingDateDesc(int categoryId);
	List<BlogContent> findAllByBlogCategoryIdAndLockedOrderByReportingDateDesc(int categoryId,boolean locked);
	List<BlogContent> findByBlogCategoryId(int categoryId);
	List<BlogContent> findByBlogCategoryIdOrderByReportingDateDesc(int categoryId);
	List<BlogContent> findAllByBlogTitleOrderByReportingDateDesc(String blogTitle);
	List<BlogContent> findAllByBlogTitleAndLockedIsOrderByReportingDateDesc(String blogTitle,boolean locked);

	List<BlogContent> findByBlogTitleOrderByReportingDateDesc(String blogTitle);
	List<BlogContent> findAllByBlogTitleAndTitleOrderByReportingDateDesc(String blogTitle, String title);
	List<BlogContent> findAllByBlogTitleAndTitleAndLockedIsOrderByReportingDateDesc(String blogTitle, String title, boolean locked);
}
