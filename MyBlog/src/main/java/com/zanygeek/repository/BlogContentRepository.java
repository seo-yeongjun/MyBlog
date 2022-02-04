package com.zanygeek.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.zanygeek.entity.BlogContent;
import com.zanygeek.entity.ContentTitle;

@Repository
public interface BlogContentRepository extends JpaRepository<BlogContent, Integer> {

	@Query(value = "select S.id, S.title,C.title as \"categoryTitle\" from blogContent S,blogCategory C where S.blogCategoryId = :categoryId && S.blogCategoryId = C.id order by reportingDate desc", nativeQuery = true)
	List<ContentTitle> findContentTitlesByBlogCategory(@Param(value = "categoryId") Integer categoryId);

	@Query(value = "select S.id, S.title,C.title as \"categoryTitle\" from blogContent S,blogCategory C where S.blogCategoryId = :categoryId && S.blogCategoryId = C.id && S.locked = false order by reportingDate desc", nativeQuery = true)
	List<ContentTitle> findContentTitlesByBlogCategoryWithOutLocked(@Param(value = "categoryId") int categoryId);

	List<BlogContent> findByBlogCategoryId(int categoryId);

	List<BlogContent> findByBlogCategoryIdOrderByReportingDateDesc(int categoryId);

	@Query(value = "select S.id, S.title,C.title as \"categoryTitle\" from blogContent S,blogCategory C where S.blogTitle = :title && S.blogCategoryId = C.id order by reportingDate desc", nativeQuery = true)
	List<ContentTitle> findContentTitlesByBlogTitle(@Param(value = "title") String title);

	@Query(value = "select S.id, S.title,C.title as \"categoryTitle\" from blogContent S,blogCategory C where S.blogTitle = :title && S.blogCategoryId = C.id && S.locked = false order by reportingDate desc", nativeQuery = true)
	List<ContentTitle> findContentTitlesByBlogTitleWithOutLocked(@Param(value = "title") String title);

	List<BlogContent> findByBlogTitle(String title);

	List<BlogContent> findByBlogTitleOrderByReportingDateDesc(String title);

	@Query(value = "select S.id, S.title,C.title as \"categoryTitle\" from blogContent S,blogCategory C where S.blogTitle = :title && S.title like %:contentTitle% && S.blogCategoryId = C.id order by reportingDate desc", nativeQuery = true)
	List<ContentTitle> findContentTitlesByBlogTitleAndContentTitle(@Param(value = "title") String title,
			@Param(value = "contentTitle") String contentTitle);

	@Query(value = "select S.id, S.title,C.title as \"categoryTitle\" from blogContent S,blogCategory C where S.blogTitle = :title && S.title like %:contentTitle% && S.blogCategoryId = C.id && S.locked = false order by reportingDate desc", nativeQuery = true)
	List<ContentTitle> findContentTitlesByBlogTitleAndContentTitleWithOutLocked(@Param(value = "title") String title,
			@Param(value = "contentTitle") String contentTitle);
}
