package com.example.video.global.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Base64;

@Configuration
public class Base64Config { 
    @Bean
    public Base64.Decoder decoder() {
        return Base64.getDecoder();
    }
}
