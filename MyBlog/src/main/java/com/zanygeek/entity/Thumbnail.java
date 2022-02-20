package com.zanygeek.entity;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
public class Thumbnail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String uploadFileName;
    private String storeFileName;
    @OneToOne
    @JoinColumn(name = "tblogContentId")
    private BlogContent blogContent;

    public Thumbnail(String uploadFileName, String storeFileName, int blogContentId) {
        this.uploadFileName = uploadFileName;
        this.storeFileName = storeFileName;
        BlogContent content = new BlogContent();
        content.setId(blogContentId);
        this.blogContent = content;
    }

    public Thumbnail() {

    }
}
