package com.example.user.dto.info;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserInfoDto {

    private String email;
    private String nickname;
    private String rankName;
    private int point;
}
