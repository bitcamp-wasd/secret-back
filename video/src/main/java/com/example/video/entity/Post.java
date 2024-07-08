package com.example.video.entity;

import com.example.video.dto.post.response.PostResponseDto;
import com.example.video.dto.post.response.MyPostDto;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Entity
@NoArgsConstructor
@DynamicInsert
public class Post {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_id")
    private Long postId;

    private String title;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="category_id")
    private Category category;

    @Column(name="thumbnail_path")
    private String thumbnailPath;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name="video_id")
    private Video video;

    private Long userId;

    @Column(name="user_nickname")
    private String userNickname;

    @Column(name="upload_date")
    private LocalDateTime uploadDate;

    @ColumnDefault("0")
    private Long views;


    @OneToMany
    private List<PostComment> postComment;



    public Post(String title,Category category, String thumbnailPath, Video video, Long userId, String userNickname) {
        this.title = title;
        this.category = category;
        this.thumbnailPath = thumbnailPath;
        this.video = video;
        this.userId = userId;
        this.userNickname = userNickname;
        this.uploadDate = LocalDateTime.now();
    }

    public PostResponseDto toPostResponseDto() {
        return new PostResponseDto(
                video.getId(),
                thumbnailPath,
                title,
                views,
                uploadDate,
                video.getLength(),
                userNickname
        );
    }

    public static MyPostDto toMyPostDto(Post post) {

        Video video = post.getVideo();

        return new MyPostDto(
                video.getId(),
                post.getThumbnailPath(),
                post.getTitle(),
                post.getViews(),
                video.getLength(),
                post.getUserNickname()
        );
    }




}
