package com.example.user.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateUserInfoDto {

    @NotBlank
    private String email;

    @NotBlank
    private String nickName;

    @NotBlank
    private String password;
}
