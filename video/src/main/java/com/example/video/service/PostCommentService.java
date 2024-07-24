package com.example.video.service;

import com.example.video.dto.auth.UserAuth;
import com.example.video.dto.comment.request.InsertCommentRequestDto;
import com.example.video.entity.Post;
import com.example.video.entity.PostComment;
import com.example.video.repository.PostCommentRepository;
import com.example.video.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PostCommentService {

    private final VideoService videoService;
    private final PostRepository postRepository;
    private final PostCommentRepository postCommentRepository;

    public void insertComment(UserAuth userAuth, InsertCommentRequestDto insertCommentRequestDto) {

        System.out.println(insertCommentRequestDto.getVideoId());
        Long postId = videoService.getPostId(insertCommentRequestDto.getVideoId());

        Post post = postRepository.findById(postId).orElseThrow(()-> new IllegalArgumentException("not found post"));

        PostComment postComment = new PostComment(userAuth.getUserId(), post, insertCommentRequestDto.getComment());

        postCommentRepository.save(postComment);
    }
}
