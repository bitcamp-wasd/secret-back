package com.example.video.dto.post.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
public class VideoApiDto {
    private Long videoId;
    private String title;
    private String thumbnail;
    private String nickname;
    private Long views;
    private LocalDateTime uploadDate;
    private int length;
}
