package com.example.gateway.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@Log4j2
@RequiredArgsConstructor
public class RedisService {
    private final RedisTemplate<String, Object> redisTemplate;


    public void set(String key, String value, long expirationTime) {
        redisTemplate.opsForValue().set(key, value, expirationTime, TimeUnit.SECONDS);
        // 로그 추가
        log.info("레디스에 저장: " + key + " -> " + value + " (" + expirationTime + "초)");
    }

    public String get(String key) {
        String value = (String) redisTemplate.opsForValue().get(key);
        // 로그 추가
        log.info("레디스에서 가져옴: " + key + " -> " + value);
        return value;
    }

    public void delete(String key) {
        redisTemplate.delete(key);
        log.info("레디스에서 삭제: " + key);
    }

    public boolean hasKey(String key) {
        return redisTemplate.hasKey(key);
    }
}
