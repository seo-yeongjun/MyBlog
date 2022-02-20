package com.zanygeek.repository;

import com.zanygeek.entity.BlogManager;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BlogManagerRepository extends JpaRepository<BlogManager, Integer> {
	public Optional<BlogManager> findByTitle(String title);
}
