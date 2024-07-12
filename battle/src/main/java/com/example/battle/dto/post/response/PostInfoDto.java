package com.example.battle.dto.post.response;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class PostInfoDto {

    private Long videoId;
    private String title;
    private String thumbnail;
    private String nickname;
    private Long views;
    private LocalDate uploadDate;
    private int length;
}
