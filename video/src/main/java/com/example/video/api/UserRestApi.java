package com.example.video.api;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;


@FeignClient(name= "user")
public interface UserRestApi {

    @GetMapping("/")
    public String test();

    @GetMapping("/test")
    public String test2();
}
