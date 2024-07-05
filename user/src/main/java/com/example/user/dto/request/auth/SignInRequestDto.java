package com.example.user.dto.request.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class SignInRequestDto {

    @Email
    @NotBlank
    private String email;

    @NotBlank
    private String password;
}
