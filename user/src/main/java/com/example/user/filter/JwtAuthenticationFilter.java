package com.example.user.filter;

import com.example.user.entity.UserEntity;
import com.example.user.provider.JwtProvider;
import com.example.user.repository.UserRepository;
import com.example.user.service.RedisService;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final Logger logger = LoggerFactory.getLogger(JwtAuthenticationFilter.class);

    private final UserRepository userRepository;
    private final JwtProvider jwtProvider;
    private final RedisService redisService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        try {
            String token = TokenUtils.parseBearerToken(request);
            logger.debug("Token: {}", token);

            if (token == null) {
                filterChain.doFilter(request, response);
                return;
            }

            Claims claims = jwtProvider.validate(token);
            if (claims == null) {
                filterChain.doFilter(request, response);
                return;
            }


            RedisService.TokenData tokenData = redisService.getTokenData(token);
            if (tokenData == null) {
                filterChain.doFilter(request, response);
                return;
            }

            Long userId = tokenData.userId;
            String role = tokenData.role;
            String nickName = tokenData.nickName;
            logger.debug("UserId: {}, Role: {}, NickName: {}", userId, role, nickName);

            UserEntity userEntity = userRepository.findById(userId)
                    .orElseThrow(() -> new IllegalArgumentException("User not found"));

            // 권한 목록 생성
            List<GrantedAuthority> authorities = new ArrayList<>();
            authorities.add(new SimpleGrantedAuthority(role));

            // SecurityContext를 생성
            SecurityContext securityContext = SecurityContextHolder.createEmptyContext();

            // 이메일 권한 사용해 인증 토큰 생성
            AbstractAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userId, null, authorities);
            authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

            securityContext.setAuthentication(authenticationToken);
            SecurityContextHolder.setContext(securityContext);

        } catch (Exception exception) {
            logger.error("Authentication error: ", exception);
        }

        filterChain.doFilter(request, response);
    }

}
