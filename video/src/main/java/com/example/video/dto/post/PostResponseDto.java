package com.example.video.dto.post;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
public class PostResponseDto {

    private Long video_id;
    private String thumbnail;
    private String title;
    private Long views;
    private LocalDate uploadDate;
    private int length;
    private String nickName;
}
