package com.example.video.dto.post.response;

import com.example.video.entity.Category;

import java.time.LocalDate;
import java.util.List;

public class VideoResponseDto {

    private Long videoId;
    private String thumbnail;
    private String title;
    private Category category;
    private String rankName;
    private Long likeCount;
    private int views;
    private LocalDate uploadDate;
    private String description;
    private List<String> sheetMusic;
    private int commentCount;
}
