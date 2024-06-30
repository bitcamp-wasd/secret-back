package com.example.user.controller;

import com.example.user.dto.UpdateUserInfoDto;
import com.example.user.dto.UserInfoBannerDto;
import com.example.user.dto.UserInfoDto;
import com.example.user.entity.CustomOAuth2User;
import com.example.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.apache.tomcat.util.http.parser.Authorization;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
public class UserController {

    private final UserService userService;

    // 확인용
    @GetMapping("/secured")
    public ResponseEntity<String> securedTest(){
        return ResponseEntity.ok("This is a secured test");
    }

    // 유저 베너
    @GetMapping("/myBanner")
    public ResponseEntity<UserInfoBannerDto> getMyBanner(Authentication authentication){
        Long userId = (Long) authentication.getPrincipal();
        UserInfoBannerDto userInfoBannerDto = userService.getUserInfoBanner(userId);
        return ResponseEntity.ok(userInfoBannerDto);
    }

    // 유저 마이페이지
    @GetMapping("/myInfo")
    public ResponseEntity<UserInfoDto> getMyInfo(Authentication authentication){
        Long userId = (Long) authentication.getPrincipal();
        UserInfoDto userInfo = userService.getUserInfo(userId);
        return ResponseEntity.ok(userInfo);
    }

    // 유저 정보 수정
    @PutMapping("/editInfo")
    public ResponseEntity<?> updateEditInfo(Authentication authentication, @RequestBody @Valid UpdateUserInfoDto updateUserInfoDto){
        Long userId = (Long) authentication.getPrincipal();
        userService.updateUser(userId, updateUserInfoDto);
        return ResponseEntity.ok().build();
    }

}
