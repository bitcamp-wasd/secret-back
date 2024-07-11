package com.example.battle.dto.info;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PostDto {

    // video에 담겨진 postId로 해당되는 videoId 가져오기
    private Long videoId;

    private String title;
    private String thumbnailPath;
    private String userNickname;
    private Long views;
    private LocalDate uploadDate;

    // 해당되는 videoId에서 영상 길이 가져오기
    private Integer length;

}
