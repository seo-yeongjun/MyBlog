package com.zanygeek.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.zanygeek.entity.BlogContent;

@Repository
public interface BlogContentRepository extends JpaRepository<BlogContent, Integer> {
}
