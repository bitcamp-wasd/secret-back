package com.example.user.dto.response.auth;

import com.example.user.common.ResponseCode;
import com.example.user.common.ResponseMessage;
import com.example.user.dto.response.ResponseDto;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@Getter
public class SignInResponseDto extends ResponseDto {

    private String accessToken;
    private String refreshToken;
    private int expirationTime;

    private SignInResponseDto(String accessToken, String refreshToken) {
        super();
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.expirationTime = 3600;
    }

    public static ResponseEntity<SignInResponseDto> success(String accessToken, String refreshToken) {
        SignInResponseDto responseBody = new SignInResponseDto(accessToken, refreshToken);
        return ResponseEntity.status(HttpStatus.OK).body(responseBody);
    }

    public static ResponseEntity<ResponseDto> signInFail() {
        ResponseDto responseBody = new ResponseDto(ResponseCode.SIGN_IN_FAIL, ResponseMessage.SIGN_IN_FAIL);
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(responseBody);
    }
}
