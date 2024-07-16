package com.example.video.controller;

import com.example.video.dto.post.response.PostIdDto;
import com.example.video.dto.post.response.VideoResponseDto;
import com.example.video.service.VideoService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class VideoController {

    private final VideoService videoService;

    /**
     * 비디오 상세 정보 가져오기
     * @param videoId
     * @return
     */
    @GetMapping("watch")
    public VideoResponseDto getVideo(@RequestParam("id") Long videoId) {
        VideoResponseDto videoResponseDto = videoService.getVideo(videoId);
        return videoResponseDto;
    }

    @GetMapping("info")
    public PostIdDto postId(@RequestParam("videoId") Long videoId) {
        Long postId = videoService.getPostId(videoId);
        return new PostIdDto(postId);
    }
}
