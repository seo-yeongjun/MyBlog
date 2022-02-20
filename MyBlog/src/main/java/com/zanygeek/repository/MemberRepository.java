package com.zanygeek.repository;

import com.zanygeek.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Integer> {
	public Optional<Member> findByUserId(String loginId);
}
