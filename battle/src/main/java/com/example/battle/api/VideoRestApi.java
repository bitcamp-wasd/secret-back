package com.example.battle.api;

import com.example.battle.dto.post.response.PostIdDto;
import com.example.battle.dto.post.response.PostInfoDto;
import com.example.battle.dto.post.response.PostUserIdDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "VideoRestApi", url = "${openfeign.url.video}")
public interface VideoRestApi {

    @GetMapping("/post/info")
    public PostInfoDto videoInfo(@RequestParam("postId") Long postId);

    @GetMapping("/info")
    public PostIdDto postIdInfo(@RequestParam("videoId") Long videoId);

    @GetMapping("/post/userinfo")
    public PostUserIdDto postUserIdInfo(@RequestParam("postId") Long postId);
}
