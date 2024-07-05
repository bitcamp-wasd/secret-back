package com.example.video.controller;

import com.example.video.dto.auth.UserAuth;
import com.example.video.global.annotation.HeaderUserAuth;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("like")
public class PostLikeController {


    @GetMapping("auth")
    public ResponseEntity<String> videoLike(@HeaderUserAuth UserAuth userAuth, @RequestParam("id") Long id) {



        return ResponseEntity.ok("처리되었습니다.");
    }
}
