package com.example.video.service;

import com.example.video.api.UserRestApi;
import com.example.video.dto.auth.UserAuth;
import com.example.video.dto.comment.request.DeleteCommentRequestDto;
import com.example.video.dto.comment.request.InsertCommentRequestDto;
import com.example.video.dto.comment.request.UpdateCommentDto;
import com.example.video.dto.comment.request.UserRequestDto;
import com.example.video.dto.comment.response.CommentResponseDto;
import com.example.video.dto.comment.response.MyCommentResponseDto;
import com.example.video.dto.comment.response.UserResponseDto;
import com.example.video.entity.Post;
import com.example.video.entity.PostComment;
import com.example.video.entity.Video;
import com.example.video.repository.PostCommentRepository;
import com.example.video.repository.PostRepository;
import com.example.video.repository.VideoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Log4j2
public class PostCommentService {

    private final VideoService videoService;
    private final VideoRepository videoRepository;
    private final PostRepository postRepository;
    private final PostCommentRepository postCommentRepository;
    private final UserRestApi userRestApi;

    public void insertComment(UserAuth userAuth, InsertCommentRequestDto insertCommentRequestDto) {

        Long postId = videoService.getPostId(insertCommentRequestDto.getVideoId());

        Post post = postRepository.findById(postId).orElseThrow(()-> new IllegalArgumentException("not found post"));

        Video video = post.getVideo();

        PostComment postComment = new PostComment(userAuth.getUserId(), post, insertCommentRequestDto.getComment());
        video.plusCommentCount();

        postCommentRepository.save(postComment);
        videoRepository.save(video);
    }

    public List<CommentResponseDto> getComment(Long videoId, Pageable page) {
        Long postId = videoService.getPostId(videoId);

        Slice<PostComment> postComment = postCommentRepository.findByPostId(postId, page);
        List<Long> userIds = postComment.stream().map((p) -> p.getUserId()).toList();

        List<UserResponseDto> userResponseDto = userRestApi.userIds(new UserRequestDto(userIds));
        Map<Long, UserResponseDto> userResponseDtoMap = userResponseDto.stream().distinct().collect(Collectors.toMap(UserResponseDto::getUserId, user -> user));


        List<CommentResponseDto> commentResponseDtos = postComment.stream()
                .map((p) -> p.toCommentResponseDto(userResponseDtoMap.get(p.getUserId())))
                .toList();

        return commentResponseDtos;
    }

    public void updateComment(UserAuth userAuth, Long commentId, UpdateCommentDto updateCommentDto) {
        PostComment postComment = postCommentRepository.findById(commentId).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 댓글입니다."));

        if(postComment.getUserId() != userAuth.getUserId())
            throw new IllegalArgumentException("권한이 없는 요청입니다.");

        postComment.updateComment(updateCommentDto.getComment());
        postCommentRepository.save(postComment);
    }

    public void deleteComments(UserAuth userAuth, DeleteCommentRequestDto deleteCommentRequestDto) {

        List<PostComment> postComments = postCommentRepository.findAllByIdFetch(deleteCommentRequestDto.getCommentIds());

        List<Video> videos = postComments.stream().map((c) -> {
            Video v = c.getPost().getVideo();
            v.minusCommentCount();
            return v;
        }).toList();

        videoRepository.saveAll(videos);
        postCommentRepository.deleteAllById(userAuth.getUserId(), deleteCommentRequestDto.getCommentIds());
    }

    public Page<MyCommentResponseDto> getMyComments(UserAuth userAuth, Pageable page) {
        Page<PostComment> postComments = postCommentRepository.findByIdFetchPost(userAuth.getUserId(), page);

        List<MyCommentResponseDto> myCommentResponseDtos = postComments.stream().map(PostComment::toMyCommentResponseDto).toList();


        return new PageImpl<>(myCommentResponseDtos, page, postComments.getTotalElements());
    }
}
