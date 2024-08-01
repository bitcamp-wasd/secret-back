package com.example.user.dto.request.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class SignUpRequestDto {

    @NotBlank
    @Email
    private String email;

    @NotBlank
    @Pattern(regexp = "^(?=.*[a-zA-Z])(?=.*[0-9])[a-zA-Z0-9]{8,13}$")
    // 8자 13자 길이의 문자열 적어도 하나 이상 알파벳 대소문자 적어도 하나이상 숫자 포함
    private String password;

    @NotBlank
    private String nickName;

    @NotBlank
    private String certificationNumber;


}
