package com.zanygeek.entity;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
public class UploadFile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String uploadFileName;
    private String storeFileName;
    @ManyToOne
    @JoinColumn(name = "blogContentId")
    private BlogContent blogContent;

    public UploadFile(String uploadFileName, String storeFileName, int blogContentId) {
        this.uploadFileName = uploadFileName;
        this.storeFileName = storeFileName;
        BlogContent content = new BlogContent();
        content.setId(blogContentId);
        this.blogContent = content;
    }

    public UploadFile() {

    }
}
