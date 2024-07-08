package com.example.video.api;

import com.example.video.dto.user.response.UserInfoDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;


@FeignClient(name= "UserRestApi", url="${openfeign.url.user}")
public interface UserRestApi {

    @GetMapping("secured")
    public String test();

    @GetMapping("/auth/secured")
    public String test2(@RequestHeader("Authorization") String authorization);

    @GetMapping("info")
    public UserInfoDto userInfo(@RequestParam("userId") Long userId);
}
