package com.example.user.dto.info.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserListDto {
    private Long userId;
    private String nickname;
    private String rankname;
}
