package com.zanygeek.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.zanygeek.entity.BlogManager;

@Repository
public interface BlogManagerRepository extends JpaRepository<BlogManager, Integer> {
	public Optional<BlogManager> findByTitle(String title);
}
