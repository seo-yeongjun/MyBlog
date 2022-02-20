package com.zanygeek.repository;

import com.zanygeek.entity.Thumbnail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface ThumbnailRepository extends JpaRepository<Thumbnail, Integer> {
    @Modifying
    @Transactional
    @Query(value ="DELETE FROM Thumbnail s WHERE s.tblogContentId = :tblogContentId",nativeQuery = true)
    void deleteAllByTblogContentId(@Param(value = "tblogContentId")int tblogContentId);
}
