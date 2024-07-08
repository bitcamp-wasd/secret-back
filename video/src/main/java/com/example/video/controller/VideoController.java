package com.example.video.controller;

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

//    @GetMapping("watch")
//    public VideoResponseDto getVideo(@RequestParam("id") Long videoId) {
//        videoService.
//    }
}
