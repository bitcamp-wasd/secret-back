package com.example.video.controller;
import com.example.video.dto.post.PostRegisterDto;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("post/")
public class PostController {


    @PostMapping
    public PostRegisterDto upload(@RequestBody PostRegisterDto postRegisterDto) {
        // Video, Post, SheetMusic 3 테이블을 건드려야 함
        return postRegisterDto;
    }
}
