package com.example.video.controller;

import com.example.video.api.UserRestApi;
import com.example.video.dto.auth.UserAuth;
import com.example.video.global.annotation.HeaderUserAuth;
import com.example.video.global.util.ParseUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import java.nio.charset.StandardCharsets;

@RestController
@RequiredArgsConstructor
@Log4j2
public class TestController {

    private final ParseUtil parseUtil;
    private final UserRestApi userRestApi;

    @GetMapping("/")
    public String test() {
        return "test routing";
    }

    @GetMapping(value = "/auth/secured")
    public ResponseEntity<String> AuthTest(@HeaderUserAuth UserAuth user) throws JsonProcessingException {
        log.info(user);
        return ResponseEntity.ok("This is a secured test");
    }



}
