package com.example.video.entity;

import jakarta.persistence.*;
import lombok.Getter;

import java.time.LocalDateTime;

@Entity
@Getter
public class SheetMusic {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name= "sheet_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "video_id")
    private Video video;

    private String path;

    private LocalDateTime uploadTime;
}