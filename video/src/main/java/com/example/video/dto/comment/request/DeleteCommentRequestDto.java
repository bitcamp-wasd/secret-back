package com.example.video.dto.comment.request;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class DeleteCommentRequestDto {
    List<Long> commentIds;
}
