package com.example.video.controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("upload/")
public class UploadController {

    @GetMapping("presigned")
    public String generatePresigned() {
        return "test string fucnk";
    }

}
