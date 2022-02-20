package com.zanygeek.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@Entity
public class BlogContent {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @OneToOne
    @JoinColumn(name = "blogCategoryId")
    private BlogCategory blogCategory;
    private String text;
    private String title;
    private int see;
    private String blogTitle;
    private boolean locked;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date reportingDate;

    @OneToOne
    @JoinColumn(name = "memberId")
    private Member member;

    @OneToOne(mappedBy = "blogContent")
    private Thumbnail thumbnail;

    @OneToMany(mappedBy = "blogContent")
    private List<UploadFile> uploadFiles;
}
