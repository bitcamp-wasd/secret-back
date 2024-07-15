package com.example.battle.api;

import com.example.battle.dto.post.response.PostInfoDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "VideoRestApi", url = "${openfeign.url.video}")
public interface VideoRestApi {

    @GetMapping("/post/info")
    public PostInfoDto videoInfo(@RequestParam("postId") Long postId);
}
