package com.example.user.controller;

import com.example.user.annotation.HeaderUserAuth;
import com.example.user.common.ParseUtil;
import com.example.user.dto.info.*;
import com.example.user.dto.info.request.UserIdRequestDto;
import com.example.user.dto.info.response.UserApiInfo;
import com.example.user.dto.info.response.UserListDto;
import com.example.user.dto.info.response.UserRankInfo;
import com.example.user.service.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    // 유저 마이페이지
    @GetMapping("/auth/myinfo")
    public ResponseEntity<UserInfoDto> getMyInfo(@HeaderUserAuth UserAuth userAuth) throws JsonProcessingException{
        Long userId = userAuth.getUserId();
        UserInfoDto userInfo = userService.getUserInfo(userId);
        return ResponseEntity.ok(userInfo);
    }

    // 유저 정보 수정
    @PutMapping("/auth/editinfo")
    public ResponseEntity<?> updateEditInfo(@HeaderUserAuth UserAuth userAuth,
                                            @RequestBody @Valid UpdateUserInfoDto updateUserInfoDto)
            throws JsonProcessingException {
        Long userId = userAuth.getUserId();
        userService.updateUser(userId, updateUserInfoDto);
        return ResponseEntity.ok().build();
    }

    @GetMapping("info")
    public ResponseEntity<UserApiInfo> getUserApiInfo(@RequestParam("userId") Long userId)
            throws JsonProcessingException {

        UserApiInfo userApiInfo = userService.getUserApiInfo(userId);
        return ResponseEntity.ok(userApiInfo);

    }

    @GetMapping("rankInfo")
    public ResponseEntity<UserRankInfo> getRankApiInfo(@RequestParam("userId") Long userId)
            throws JsonProcessingException {

        UserRankInfo userRankInfo = userService.getRankApiInfo(userId);
        return ResponseEntity.ok(userRankInfo);
    }

    @PutMapping("pointInfo")
    public ResponseEntity<Void> addUserPoints(@RequestParam("userId") Long userId,
                                              @RequestParam("point") int point)
            throws JsonProcessingException {
        userService.addUserPoints(userId, point);
        return ResponseEntity.ok().build();
    }

    @PostMapping("listInfo")
    public ResponseEntity<List<UserListDto>> getUserList(@RequestBody UserIdRequestDto request){
        List<Long> userId = request.getUserId();
        List<UserListDto> userList = userService.getUserList(userId);
        return ResponseEntity.ok(userList);
    }

}
