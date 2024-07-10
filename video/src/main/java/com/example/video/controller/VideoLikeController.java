package com.example.video.controller;

import com.example.video.dto.auth.UserAuth;
import com.example.video.global.annotation.HeaderUserAuth;
import com.example.video.service.VideoLikeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
@RequestMapping("like")
public class VideoLikeController {

    private final VideoLikeService videoLikeService;

    @GetMapping("auth/check")
    public ResponseEntity<Boolean> likeCheck(@HeaderUserAuth UserAuth userAuth, @RequestParam("id") Long videoId) {
        Boolean check = videoLikeService.isLike(userAuth, videoId);
        return ResponseEntity.ok(check);
    }

    @GetMapping("auth")
    public ResponseEntity<String> videoLike(@HeaderUserAuth UserAuth userAuth, @RequestParam("id") Long videoId) {
        videoLikeService.videoLike(userAuth, videoId);
        return ResponseEntity.ok("처리되었습니다.");
    }


}
