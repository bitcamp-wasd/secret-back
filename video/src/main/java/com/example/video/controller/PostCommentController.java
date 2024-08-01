package com.example.video.controller;

import com.example.video.dto.auth.UserAuth;
import com.example.video.dto.comment.request.DeleteCommentRequestDto;
import com.example.video.dto.comment.request.InsertCommentRequestDto;
import com.example.video.dto.comment.request.UpdateCommentDto;
import com.example.video.dto.comment.response.CommentResponseDto;
import com.example.video.dto.comment.response.MyCommentResponseDto;
import com.example.video.global.annotation.HeaderUserAuth;
import com.example.video.service.PostCommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class PostCommentController {

    private final PostCommentService postCommentService;

    @PostMapping("/auth/comment")
    public ResponseEntity<String> insertComment(@HeaderUserAuth UserAuth userAuth, @RequestBody InsertCommentRequestDto insertCommentRequestDto) {

        postCommentService.insertComment(userAuth, insertCommentRequestDto);
        return ResponseEntity.ok("ok");
    }

    @GetMapping("comment")
    public ResponseEntity<List<CommentResponseDto>> getComment(@RequestParam("videoId") Long videoId, @RequestParam("pageNumber") int pageNumber) {
        Pageable page = PageRequest.of(pageNumber, 10, Sort.by("createDate").descending());
        List<CommentResponseDto> commentResponseDto = postCommentService.getComment(videoId, page);
        return ResponseEntity.ok(commentResponseDto);
    }

    @PatchMapping("/auth/comment")
    public ResponseEntity<String> updateComment(@HeaderUserAuth UserAuth userAuth, @RequestParam("commentId") Long commentId, @RequestBody UpdateCommentDto updateCommentDto) {

        postCommentService.updateComment(userAuth, commentId, updateCommentDto);

        return ResponseEntity.ok("ok");
    }

    @DeleteMapping("/auth/comment")
    public ResponseEntity<String> deleteComments(@HeaderUserAuth UserAuth userAuth, @RequestBody DeleteCommentRequestDto deleteCommentRequestDto) {
        postCommentService.deleteComments(userAuth, deleteCommentRequestDto);

        return ResponseEntity.ok("ok");
    }

    @GetMapping("/auth/mycomment")
    public ResponseEntity<Page<MyCommentResponseDto>> myComments(@HeaderUserAuth UserAuth userAuth, @RequestParam("pageNumber") int pageNumber) {
        Pageable page = PageRequest.of(pageNumber, 10, Sort.by("createDate").descending());
        Page<MyCommentResponseDto> myCommentResponseDtos = postCommentService.getMyComments(userAuth, page);
        return ResponseEntity.ok(myCommentResponseDtos);
    }
}
