package com.example.battle.util;

import com.example.battle.dto.auth.UserAuth;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class ParseUtil {
    private final ObjectMapper objectMapper;

    public UserAuth parseAuth(String json) throws JsonProcessingException {
        UserAuth userAuth = objectMapper.readValue(json, UserAuth.class);
        return userAuth;
    }

}
