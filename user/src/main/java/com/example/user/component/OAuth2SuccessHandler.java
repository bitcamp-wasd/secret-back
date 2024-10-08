package com.example.user.component;

import com.example.user.entity.CustomOAuth2User;
import com.example.user.provider.JwtProvider;
import com.example.user.service.RedisService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class OAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final JwtProvider jwtProvider;
    private final RedisService redisService;

    @Override
    public void onAuthenticationSuccess(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication) throws IOException, ServletException {

        CustomOAuth2User oAuth2User = (CustomOAuth2User) authentication.getPrincipal();

        Long userId = oAuth2User.getUserId();
        String role = oAuth2User.getRole();
        String nickName = oAuth2User.getNickName();

        // Access Token 생성
        String accessToken = jwtProvider.create(userId, role, nickName, 3600);

        // Refresh Token 생성 및 Redis에 저장
        String refreshToken = jwtProvider.create(userId, role, nickName, 604800); // 7일

        redisService.setTokenData(accessToken, userId, role, nickName, 3600);
        redisService.setTokenData(refreshToken, userId, role, nickName, 604800);

        response.addHeader(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken);

        // Refresh Token을 쿠키에 저장
        Cookie refreshTokenCookie  = new Cookie("refresh_token", refreshToken);
        refreshTokenCookie.setHttpOnly(true);   // JavaScript를 통한 접근 방지
        refreshTokenCookie.setSecure(true);     // HTTPS를 통해서만 쿠키 전송
        refreshTokenCookie.setPath("/");
        refreshTokenCookie.setMaxAge(604800); // 7일
        response.addCookie(refreshTokenCookie);

        response.sendRedirect("http://localhost:3000/oauth/callback?accessToken=" + accessToken + "&refreshToken=" + refreshToken);
    }

}
