package com.example.user.config;

import com.example.user.component.AuthEntryPoint;
import com.example.user.component.CustomAccessDeniedHandler;
import com.example.user.component.OAuth2SuccessHandler;
import com.example.user.filter.JwtAuthenticationFilter;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.config.annotation.web.configurers.HttpBasicConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configurable
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class WebSecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final OAuth2SuccessHandler oAuth2SuccessHandler;
    private final DefaultOAuth2UserService oAuth2UserService;
    private final AuthEntryPoint authEntryPoint;
    private final CustomAccessDeniedHandler customAccessDeniedHandler;

    @Bean
    protected SecurityFilterChain configure(HttpSecurity httpSecurity) throws Exception {

        httpSecurity
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                // 외부 POST 요청을 받아야하니 csrf는 꺼준다.
                // csrf보안은 세션을 활용하는데 Rest서버는 세션을 사용하지 않으므로 disable
                .csrf(CsrfConfigurer::disable)
                .httpBasic(HttpBasicConfigurer::disable)
                .sessionManagement(sessionManagement -> sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                .authorizeHttpRequests(request -> request
                        // 특정 경로에 대해 모든 사용자에게 접근 허용
                        .requestMatchers("/", "api/auth/**", "/oauth2/**").permitAll()
                        // user 권한 가진 사용자만 접근 허용
                        .requestMatchers("api/user/**").hasRole("USER")
                        // admin 권한 가진 사용자만 접근 허용
                        .requestMatchers("api/admin/**").hasRole("ADMIN")
                        .anyRequest().authenticated())

                .oauth2Login(oauth2 -> oauth2
                        // OAuth2 로그인 설정, 인증 엔드포인트 기본 url 설정
                        .authorizationEndpoint(endpoint -> endpoint.baseUri("/api/auth/oauth2"))
                        // 리다이렉션 엔드포인트 기본 url 설정
                        .redirectionEndpoint(endpoint -> endpoint.baseUri("/oauth2/callback/*"))
                        .userInfoEndpoint(endpoint -> endpoint.userService(oAuth2UserService))
                        .successHandler(oAuth2SuccessHandler))

                .exceptionHandling(exceptionHandling -> exceptionHandling
                        .authenticationEntryPoint(authEntryPoint) // 401 처리
                        .accessDeniedHandler(customAccessDeniedHandler)) // 403 처리
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return httpSecurity.build();

    }

    @Bean
    protected CorsConfigurationSource corsConfigurationSource() {

        CorsConfiguration corsConfiguration = new CorsConfiguration();
        corsConfiguration.addAllowedOrigin("*");
        corsConfiguration.addAllowedMethod("*");
        corsConfiguration.addAllowedHeader("*");

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", corsConfiguration);

        return source;
    }
}
