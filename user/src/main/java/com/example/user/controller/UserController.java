package com.example.user.controller;

import com.example.user.annotation.HeaderUserAuth;
import com.example.user.common.ParseUtil;
import com.example.user.dto.UpdateUserInfoDto;
import com.example.user.dto.UserAuth;
import com.example.user.dto.UserInfoBannerDto;
import com.example.user.dto.UserInfoDto;
import com.example.user.service.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@Log4j2
public class UserController {

    private final UserService userService;
    private final ParseUtil parseUtil;

    // 확인용
    @GetMapping("/secured")
    public ResponseEntity<String> securedTest(){
        return ResponseEntity.ok("This is a secured test");
    }

    @GetMapping(value = "/auth/secured", headers = "user")
    public ResponseEntity<String> AuthTest(@HeaderUserAuth UserAuth userAuth) throws JsonProcessingException {

        log.info(userAuth);

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
