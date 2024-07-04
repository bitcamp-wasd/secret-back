package com.example.user.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class UserAuth {
    private Long userId;
    private String role;
    private String nickName;
}
