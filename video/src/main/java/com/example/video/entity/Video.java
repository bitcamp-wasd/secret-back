package com.example.video.entity;

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


    @OneToMany(mappedBy = "video")
    List<SheetMusic> sheetMusicList;

    public Video(String videoPath, String description, int length) {
        this.videoPath = videoPath;
        this.description = description;
        this.length = length;
        this.state = VideoState.UPLOADING;
    }
}
