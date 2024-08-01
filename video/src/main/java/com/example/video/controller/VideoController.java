package com.example.video.controller;

import com.example.video.dto.auth.UserAuth;
import com.example.video.dto.post.response.PostIdDto;
import com.example.video.dto.video.request.UpdateRequestDto;
import com.example.video.dto.video.response.UpdateResponseDto;
import com.example.video.dto.video.response.VideoResponseDto;
import com.example.video.global.annotation.HeaderUserAuth;
import com.example.video.service.VideoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @PatchMapping("auth/update")
    public ResponseEntity<UpdateResponseDto> update(@HeaderUserAuth UserAuth userAuth,@RequestBody UpdateRequestDto updateRequestDto, @RequestParam("videoId") Long videoId) throws IllegalAccessException {

        UpdateResponseDto updateResponseDto = videoService.updateVideo(userAuth, videoId, updateRequestDto);
        return ResponseEntity.ok(updateResponseDto);
    }
}
