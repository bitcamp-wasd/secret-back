package com.example.video.dto.post.response;

import com.example.video.entity.Category;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class VideoResponseDto {

    private Long videoId;
    private String thumbnail;
    private String title;
    private String category;
    private String nickname;
    private String rankName;
    private Long likeCount;
    private Long views;
    private LocalDateTime uploadDate;
    private String description;
    private List<String> sheetMusic;
    private Long commentCount;
}
