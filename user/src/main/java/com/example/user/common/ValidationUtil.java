package com.example.user.common;

import org.springframework.stereotype.Component;

@Component
public class ValidationUtil {

    public boolean isValidNickname(String nickname) {
        String nicknameFormat = "^[가-힣a-zA-Z0-9]{2,13}$";
        return nickname != null && nickname.matches(nicknameFormat);
    }
}
