package com.example.video.dto.comment.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
public class MyCommentResponseDto {
    private Long commentId;
    private String comment;
    private String title;
    private LocalDateTime createDate;
    private Long videoId;

}
