package com.zanygeek.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.zanygeek.entity.Member;

@Repository
public interface MemberRepository extends JpaRepository<Member, Integer> {
	public Optional<Member> findByUserId(String loginId);
}
