package com.example.video.dto.auth;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@AllArgsConstructor
public class UserAuth {
    private Long userId;
    private String role;
    private String nickName;
}
