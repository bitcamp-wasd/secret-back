package com.example.user.dto.info.request;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class UserIdRequestDto {
    private List<Long> userId;

}
