package com.example.video.entity;

import com.example.video.entity.state.VideoState;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
public class Video {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name= "video_id")
    private Long id;

    @Column(name="video_path")
    private String videoPath;

    private String description;

    private Long length;

    @Column(name="like_count")
    private Long likeCount;

    @Column(name="comment_count")
    private Long commnetCount;

    @Enumerated(EnumType.STRING)
    private VideoState state;


}
