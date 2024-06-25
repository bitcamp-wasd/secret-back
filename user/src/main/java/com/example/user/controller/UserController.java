package com.example.user.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
public class UserController {

    // 확인용
    @GetMapping("/secured")
    public ResponseEntity<String> securedTest(){
        return ResponseEntity.ok("This is a secured test");
    }
}
