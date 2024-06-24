package com.example.video.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Entity
public class Post {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_id")
    private Long postId;

    private String title;


    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="category_id")
    private Category category;

    @Column(name="thumbnail_path")
    private String thumbnail_path;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="video_id")
    private Video video;

    @Column(name="user_nickname")
    private String userNickname;

    @Column(name="upload_date")
    private LocalDate uploadDate;

    private Long views;


    @OneToMany(mappedBy = "post")
    private List<PostComment> postComment;

}
