package com.zanygeek.repository;

import com.zanygeek.entity.BlogCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BlogCategoryRepository extends JpaRepository<BlogCategory, Integer> {
	public List<BlogCategory> findByBlogManagerId(int blogManagerId);
	public BlogCategory findByBlogManagerIdAndTitle(int blogManagerId, String categoryTitle);
}
