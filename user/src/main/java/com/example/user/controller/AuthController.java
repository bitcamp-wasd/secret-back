package com.example.user.controller;

import com.example.user.dto.request.auth.*;
import com.example.user.dto.response.auth.*;
import com.example.user.filter.TokenUtils;
import com.example.user.provider.JwtProvider;
import com.example.user.service.AuthService;
import com.example.user.service.RedisService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;
    private final JwtProvider jwtProvider;
    private final RedisService redisService;

    @PostMapping("/email-certification")
    public ResponseEntity<? super EmailCertificationResponseDto> emailCertification(
            @RequestBody @Valid EmailCertificationRequestDto requestBody){
        ResponseEntity<? super EmailCertificationResponseDto> response = authService.emailCertification(requestBody);
        return response;
    }

    @PostMapping("/check-certification")
    public ResponseEntity<? super CheckCertificationResponseDto> checkCertification(
            @RequestBody @Valid CheckCertificationRequestDto requestBody){
        ResponseEntity<? super CheckCertificationResponseDto> response = authService.checkCertification(requestBody);
        return response;
    }

    @PostMapping("/nickName-check")
    public ResponseEntity<? super NickNameCheckResponseDto> nickNameCheck(
            @RequestBody @Valid NickNameCheckRequestDto requestBody){
        ResponseEntity<? super NickNameCheckResponseDto> response = authService.nickNameCheck(requestBody);
        return response;
    }

    @PostMapping("/sign-up")
    public ResponseEntity<? super SignUpResponseDto> signUp(
            @RequestBody @Valid SignUpRequestDto requestBody){
        ResponseEntity<? super SignUpResponseDto> response = authService.signUp(requestBody);
        return response;
    }

    @PostMapping("logout")
    public ResponseEntity<?> logout(HttpServletRequest request, HttpServletResponse response) {
        authService.logout(request,response);

        // 응답 헤더에서 액세스 토큰 제거
        response.setHeader(HttpHeaders.AUTHORIZATION, "");

        return ResponseEntity.ok().body("logged out successfully");
    }

    @PostMapping("/sign-in")
    public ResponseEntity<? super SignInResponseDto> signIn(
            @RequestBody @Valid SignInRequestDto requestBody, HttpServletResponse response){
        ResponseEntity<? super SignInResponseDto> responseEntity  = authService.signIn(requestBody);
        if (responseEntity.getStatusCode().is2xxSuccessful()){
            SignInResponseDto responseBody = (SignInResponseDto) responseEntity.getBody();
            String accessToken = responseBody.getAccessToken();
            String refreshToken = responseBody.getRefreshToken();

            Cookie refreshTokenCookie = new Cookie("refresh_token", refreshToken);
            refreshTokenCookie.setHttpOnly(true);
            refreshTokenCookie.setSecure(true);
            refreshTokenCookie.setPath("/");
            refreshTokenCookie.setMaxAge(604800); // 7일
            response.addCookie(refreshTokenCookie);

            response.addHeader(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken);
        }
        return responseEntity;
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<?> refreshToken(HttpServletRequest request, HttpServletResponse response) {
        String refreshToken = TokenUtils.parseBearerToken(request);

        if (refreshToken != null){
            String email = jwtProvider.validate(refreshToken);
            if (email != null){
                String storedRefreshToken = redisService.get("refresh:" + email);
                if (refreshToken.equals(storedRefreshToken)){
                    String newAccessToken = jwtProvider.create(email, 3600);

                    // 응답 헤더에 새로 발급된 액세스 토큰 포함
                    response.addHeader(HttpHeaders.AUTHORIZATION, "Bearer " + newAccessToken);

                    return ResponseEntity.ok(newAccessToken);
                }
            }
        }

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid refresh token");
    }

}
