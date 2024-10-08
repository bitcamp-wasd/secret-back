package com.example.video.entity;

import com.example.video.dto.video.response.VideoResponseDto;
import com.example.video.dto.user.response.UserInfoDto;
import com.example.video.entity.state.VideoState;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;

import java.util.List;

@Entity
@Getter
@NoArgsConstructor
@DynamicInsert
public class Video {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name= "video_id")
    private Long id;

    @Column(name="video_path")
    private String videoPath;

    private String description;

    private int length;

    @Column(name="like_count")
    @ColumnDefault("0")
    private Long likeCount;

    @Column(name="comment_count")
    @ColumnDefault("0")
    private Long commentCount;

    @Enumerated(EnumType.STRING)
    private VideoState state;


    @OneToMany(mappedBy = "video", cascade = CascadeType.ALL, orphanRemoval = true)
    List<SheetMusic> sheetMusicList;

    @OneToOne(mappedBy = "video", fetch = FetchType.LAZY)
    Post post;

    @OneToMany(mappedBy = "video", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    List<VideoLikeList> videoLikeList;


    public Video(String videoPath, String description, int length) {
        this.videoPath = videoPath;
        this.description = description;
        this.length = length;
        this.state = VideoState.UPLOADING;
    }

    public static VideoResponseDto toVideoReponseDto(Video video, UserInfoDto userInfoDto) {
        Post post = video.getPost();

        return new VideoResponseDto(
                video.getId(),
                post.getThumbnailPath(),
                video.getVideoPath(),
                post.getTitle(),
                post.getCategory().getCategory(),
                userInfoDto.getNickname(),
                userInfoDto.getRankName(),
                video.getLikeCount(),
                post.getViews(),
                post.getUploadDate(),
                video.getDescription(),
                video.getSheetMusicList().stream().map((s) -> s.getPath()).toList(),
                video.getCommentCount()
        );
    }

    public void updateVideo(Category category, String description, String title) {
        this.post.updateTitle(title);
        this.post.updateCategory(category);
        this.updateDescription(description);
    }

    public void updateSheetMusic(List<SheetMusic> sheetMusicList) {
        this.sheetMusicList.clear();
        sheetMusicList.stream().forEach((sheetMusic) -> {
            this.sheetMusicList.add(sheetMusic);
        });
    }

    public void updateDescription(String description) {
        this.description = description;
    }

    // 좋아요 수
    public void plusLikeCount() {
        this.likeCount += 1;
    }
    public void minusLikeCount() {
        this.likeCount -= 1;
    }

    public void plusCommentCount() {this.commentCount += 1;}
    public void minusCommentCount() {this.commentCount -= 1;}
}
