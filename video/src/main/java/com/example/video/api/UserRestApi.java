package com.example.video.api;

import com.example.video.dto.comment.request.UserRequestDto;
import com.example.video.dto.comment.response.UserResponseDto;
import com.example.video.dto.user.response.UserInfoDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@FeignClient(name= "UserRestApi", url="${openfeign.url.user}")
public interface UserRestApi {

    @GetMapping("info")
    public UserInfoDto userInfo(@RequestParam("userId") Long userId);

    @PostMapping("listInfo")
    public List<UserResponseDto> userIds(@RequestBody UserRequestDto userRequestDto);
}
