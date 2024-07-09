package com.example.video.controller;
import com.example.video.dto.auth.UserAuth;
import com.example.video.dto.post.request.PostListDto;
import com.example.video.dto.post.request.PostRegisterDto;
import com.example.video.dto.post.response.PostRegisterResponseDto;
import com.example.video.dto.post.response.PostResponseDto;
import com.example.video.dto.post.response.MyPostDto;
import com.example.video.entity.Post;
import com.example.video.global.annotation.HeaderUserAuth;
import com.example.video.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequiredArgsConstructor
@RequestMapping("post")
public class PostController {

    private final PostService postService;

    /**
     * 게시물을 카테고리 별로 필터 정렬하여 전송
     * (미완)
     * @param pageNumber
     * @param
     * @return
     */
    @PostMapping()
    public ResponseEntity<List<PostResponseDto>> getPostList(@RequestParam("pageNumber") int pageNumber, @RequestBody PostListDto postListDto) {

        Pageable pageable = PageRequest.of(pageNumber, 16, Sort.by(postListDto.getSort()).descending());
        List<PostResponseDto>  response = postService.getPostList(postListDto.getCategory(),pageable).map(Post::toPostResponseDto).toList();
        return ResponseEntity.ok(response);
    }


    /**
     * 게시물 등록
     * @param postRegisterDto 게시물 정보
     * @return 동영상, 이미지를 업로드하는데 사용될 Presigned URL
     */
    @PostMapping("auth")
    public ResponseEntity<PostRegisterResponseDto> upload(@HeaderUserAuth UserAuth user, @RequestBody PostRegisterDto postRegisterDto) {
        // Video, Post, SheetMusic 3 테이블을 건드려야 함
        PostRegisterResponseDto responseDto = postService.posting(user,postRegisterDto);
        return ResponseEntity.ok(responseDto);
    }


    /**
     * 게시물 삭제
     * @param user
     * @param id
     * @return
     */
    @DeleteMapping("auth")
    public ResponseEntity<Boolean> deletePost(@HeaderUserAuth UserAuth user, @RequestParam("id") Long id) {
        postService.deletePost(user, id);
        return ResponseEntity.ok(true);
    }

    @GetMapping("auth/myposts")
    public ResponseEntity<List<MyPostDto>> myPosts(@HeaderUserAuth UserAuth user, @RequestParam("pageNumber") int pageNumber) {
        Pageable page = PageRequest.of(pageNumber, 10, Sort.by("uploadDate").descending());
        List<MyPostDto> myPosts = postService.myPost(user, page);
        return ResponseEntity.ok(myPosts);
    }

}
