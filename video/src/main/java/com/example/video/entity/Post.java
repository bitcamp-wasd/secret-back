package com.example.video.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Entity
@NoArgsConstructor
public class Post {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_id")
    private Long postId;

    private String title;


    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="category_id")
    private Category category;

    @Column(name="thumbnail_path")
    private String thumbnailPath;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="video_id")
    private Video video;

    @Column(name="user_nickname")
    private String userNickname;

    @Column(name="upload_date")
    private LocalDate uploadDate;

    @ColumnDefault("0")
    private Long views;


    @OneToMany(mappedBy = "post")
    private List<PostComment> postComment;


    public Post(String title,Category category, String thumbnailPath, Video video, String userNickname) {
        this.title = title;
        this.category = category;
        this.thumbnailPath = thumbnailPath;
        this.video = video;
        this.userNickname = userNickname;
        this.uploadDate = LocalDate.now();
    }


}
