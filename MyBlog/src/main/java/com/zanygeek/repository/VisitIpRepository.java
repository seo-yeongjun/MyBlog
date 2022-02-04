package com.zanygeek.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.zanygeek.entity.VisitIp;

@Repository
public interface VisitIpRepository extends JpaRepository<VisitIp, Integer> {
	public VisitIp findByIpAndTitle(String ip, String title);
	public VisitIp findByIpAndContentId(String ip, int contentId);
	@Modifying
	@Transactional
	@Query(value ="update blogManager set visitToday = default",nativeQuery = true)
	public void resetToday();
}
