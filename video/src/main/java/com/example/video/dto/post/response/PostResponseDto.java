package com.example.video.dto.post.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
public class PostResponseDto {

    private Long videoId;
    private String thumbnail;
    private String title;
    private Long views;
    private LocalDateTime uploadDate;
    private int length;
    private String nickname;
    private String category;
}
