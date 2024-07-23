package com.example.video.dto.comment.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class InsertCommentRequestDto {
    private Long videoId;
    private String comment;
}
