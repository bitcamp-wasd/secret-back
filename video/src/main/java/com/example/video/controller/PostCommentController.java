package com.example.video.controller;

import com.example.video.dto.auth.UserAuth;
import com.example.video.dto.comment.request.InsertCommentRequestDto;
import com.example.video.global.annotation.HeaderUserAuth;
import com.example.video.service.PostCommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Controller
@RequiredArgsConstructor
public class PostCommentController {

    private final PostCommentService postCommentService;

    @PostMapping("/auth/comment")
    public ResponseEntity<String> insertComment(@HeaderUserAuth()UserAuth userAuth, @RequestBody InsertCommentRequestDto insertCommentRequestDto) {

        postCommentService.insertComment(userAuth, insertCommentRequestDto);
        return ResponseEntity.ok("ok");
    }
}
