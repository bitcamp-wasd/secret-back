package com.example.user.controller;

import com.example.user.dto.request.auth.*;
import com.example.user.dto.response.auth.*;
import com.example.user.filter.TokenUtils;
import com.example.user.provider.JwtProvider;
import com.example.user.service.AuthService;
import com.example.user.service.RedisService;
import com.fasterxml.jackson.core.JsonProcessingException;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Log4j2
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
    public ResponseEntity<?> refreshToken(HttpServletRequest request, HttpServletResponse response) throws JsonProcessingException {
        String refreshToken = TokenUtils.parseRefreshToken(request);

        if (refreshToken != null){
            Claims claims = jwtProvider.validate(refreshToken);
            if (claims != null){
                RedisService.TokenData tokenData = redisService.getTokenData(refreshToken);
                if (tokenData != null) {
                    Long userId = tokenData.userId;
                    String role = tokenData.role;
                    String nickName = tokenData.nickName;
                    String newAccessToken = jwtProvider.create(userId, role, nickName,3600);

                    // Redis에 새 엑세스 토큰 저장
                    redisService.setTokenData(newAccessToken, userId, role, nickName,3600);

                    log.info(redisService.getTokenData(newAccessToken));
                    // 응답 헤더에 새로 발급된 액세스 토큰 포함
                    response.addHeader(HttpHeaders.AUTHORIZATION, "Bearer " + newAccessToken);

                    return ResponseEntity.ok(newAccessToken);
                }
            }
        }

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid refresh token");
    }

}
