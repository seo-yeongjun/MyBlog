package com.zanygeek.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.zanygeek.entity.BlogCategory;

@Repository
public interface BlogCategoryRepository extends JpaRepository<BlogCategory, Integer> {
	public List<BlogCategory> findByBlogManagerId(int blogManagerId);
}
