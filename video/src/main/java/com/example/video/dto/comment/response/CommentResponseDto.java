package com.example.video.dto.comment.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
public class CommentResponseDto {
    private Long commentId;
    private String nickname;
    private String rankName;
    private String comment;
    private LocalDateTime createDate;
}
