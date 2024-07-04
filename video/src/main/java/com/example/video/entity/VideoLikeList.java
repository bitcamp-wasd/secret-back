package com.example.video.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class VideoLikeList {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name= "list_id")
    private Long listId;

    @ManyToOne
    @JoinColumn(name="video_id")
    private Video video;

    @Column(name="userId")
    private Long userId;


    public VideoLikeList(Video video, Long userId) {
        this.video = video;
        this.userId = userId;
    }

}
