package com.example.user.dto.info;

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
