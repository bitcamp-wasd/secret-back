package com.example.video.dto.post.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class MyPostDto {

    private Long videoId;
    private String thumbnail;
    private String title;
    private Long views;
    private int length;
    private String nickname;

    public MyPostDto(Long videoId, String thumbnail, String title, Long views, int length, String nickname) {
        this.videoId = videoId;
        this.thumbnail = thumbnail;
        this.title = title;
        this.views = views;
        this.length = length;
        this.nickname = nickname;
    }
}
