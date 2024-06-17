package com.example.video.controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class UploadController {

    @GetMapping
    public String test() {
        return "test string fucnk";
    }

}
