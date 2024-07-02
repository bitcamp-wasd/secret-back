package com.example.video.controller;
import com.example.video.dto.auth.UserAuth;
import com.example.video.dto.post.PostRegisterDto;
import com.example.video.dto.post.PostRegisterResponseDto;
import com.example.video.dto.post.PostResponseDto;
import com.example.video.global.annotation.HeaderUserAuth;
import com.example.video.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
@RequestMapping("post/")
public class PostController {

    private final PostService postService;

    /**
     * 게시물 등록
     * @param postRegisterDto 게시물 정보
     * @return 동영상, 이미지를 업로드하는데 사용될 Presigned URL
     */
    @PostMapping()
    public ResponseEntity<PostRegisterResponseDto> upload(@HeaderUserAuth UserAuth user, @RequestBody PostRegisterDto postRegisterDto) {
        // Video, Post, SheetMusic 3 테이블을 건드려야 함
        PostRegisterResponseDto responseDto = postService.posting(user,postRegisterDto);
        return ResponseEntity.ok(responseDto);
    }

    @GetMapping()
    public ResponseEntity<Slice<PostResponseDto>> getPostList(@RequestParam("pageNumber") int pageNumber, @RequestParam("category") String category) {

        Pageable pageable = PageRequest.of(pageNumber, 16);


        return null;
    }
}
