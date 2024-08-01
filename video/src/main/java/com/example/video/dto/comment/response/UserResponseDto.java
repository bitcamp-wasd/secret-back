package com.example.video.dto.comment.response;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class UserResponseDto {
    private Long userId;
    private String nickname;
    private String rankname;
}
