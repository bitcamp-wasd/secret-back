package com.example.user.service;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class RedisService {

    private final RedisTemplate<String, Object> redisTemplate;

    public RedisService(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public void set(String key, String value, long expirationTime) {
        redisTemplate.opsForValue().set(key, value, expirationTime, TimeUnit.SECONDS);
        // 로그 추가
        System.out.println("레디스에 저장: " + key + " -> " + value + " (" + expirationTime + "초)");
    }

    public String get(String key) {
        String value = (String) redisTemplate.opsForValue().get(key);
        // 로그 추가
        System.out.println("레디스에서 가져옴: " + key + " -> " + value);
        return value;
    }

    public void delete(String key) {
        redisTemplate.delete(key);
        System.out.println("레디스에서 삭제: " + key);
    }
}