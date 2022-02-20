package com.zanygeek.repository;

import com.zanygeek.entity.UploadFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UploadFileRepository extends JpaRepository<UploadFile, Integer> {
    void deleteAllByBlogContentId(int contentId);
}
