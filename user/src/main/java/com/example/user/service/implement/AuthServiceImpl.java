package com.example.user.service.implement;

import com.example.user.common.CertificationNumber;
import com.example.user.common.ValidationUtil;
import com.example.user.dto.request.auth.*;
import com.example.user.dto.response.ResponseDto;
import com.example.user.dto.response.auth.*;
import com.example.user.entity.UserEntity;
import com.example.user.entity.UserRankEntity;
import com.example.user.filter.TokenUtils;
import com.example.user.provider.EmailProvider;
import com.example.user.provider.JwtProvider;
import com.example.user.repository.UserRankRepository;
import com.example.user.repository.UserRepository;
import com.example.user.service.AuthService;
import com.example.user.service.RedisService;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Log4j2
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final RedisService redisService;
    private final JwtProvider jwtProvider;
    private final EmailProvider emailProvider;
    private final UserRankRepository userRankRepository;
    private final ValidationUtil validationUtil;

    private PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Override
    public ResponseEntity<? super NickNameCheckResponseDto> nickNameCheck(NickNameCheckRequestDto dto) {

        try {

            String nickName = dto.getNickName();

            if (!validationUtil.isValidNickname(nickName)){
                return NickNameCheckResponseDto.invalidNickName();
            }

            boolean isExistNickName = userRepository.existsByNickname(nickName);
            if (isExistNickName)
                return NickNameCheckResponseDto.duplicateNickName();

        }catch (Exception exception){
            exception.printStackTrace();
            return ResponseDto.databaseError();
        }

        return NickNameCheckResponseDto.success();
    }

    @Override
    public ResponseEntity<? super EmailCertificationResponseDto> emailCertification(EmailCertificationRequestDto dto) {

        try {

            String email = dto.getEmail();

            boolean isExistEmail = userRepository.existsByEmail(email);
            if (isExistEmail)
                return EmailCertificationResponseDto.duplicateEmail();


            String certificationNumber = CertificationNumber.getCertificationNumber();

            boolean isSuccessed = emailProvider.sendCertificationMail(email, certificationNumber);
            if (!isSuccessed)
                return EmailCertificationResponseDto.mailSendFail();

            redisService.set("certification:" + email, certificationNumber, 300);


        }catch (Exception exception){
            exception.printStackTrace();
            return ResponseDto.databaseError();
        }

        return EmailCertificationResponseDto.success();
    }

    @Override
    public ResponseEntity<? super CheckCertificationResponseDto> checkCertification(CheckCertificationRequestDto dto) {

        try {

            String email = dto.getEmail();
            String certificationNumber = dto.getCertificationNumber();

            String storedCertificationNumber = redisService.get("certification:" + email);
            if (storedCertificationNumber == null || !storedCertificationNumber.equals(certificationNumber)) {
                return CheckCertificationResponseDto.certificationFail();
            }

        }catch (Exception exception){
            exception.printStackTrace();
            return ResponseDto.databaseError();
        }

        return CheckCertificationResponseDto.success();
    }

    @Override
    public ResponseEntity<? super SignUpResponseDto> signUp(SignUpRequestDto dto) {

        try {

            String email = dto.getEmail();

            boolean isExistEmail = userRepository.existsByEmail(email);
            if (isExistEmail)
                return SignUpResponseDto.duplicateEmail();

            String certificationNumber = dto.getCertificationNumber();

            String storedCertificationNumber = redisService.get("certification:" + email);
            if (storedCertificationNumber == null || !storedCertificationNumber.equals(certificationNumber)) {
                return SignUpResponseDto.certificationFail();
            }

            String password = dto.getPassword();
            String encodePassword = passwordEncoder.encode(password);
            dto.setPassword(encodePassword);

            String nickName = dto.getNickName();

            if (!validationUtil.isValidNickname(nickName)){
                return NickNameCheckResponseDto.invalidNickName();
            }

            boolean isExistNickName = userRepository.existsByNickname(nickName);
            if (isExistNickName)
                return NickNameCheckResponseDto.duplicateNickName();

            UserRankEntity defaultRank = userRankRepository.findByMinPoint(0)
                    .orElseThrow(() -> new IllegalArgumentException("Default rank not found"));

            UserEntity userEntity = new UserEntity(dto, defaultRank);
            userRepository.save(userEntity);

            redisService.delete("certification:" + email);



        }catch (Exception exception){
            exception.printStackTrace();
            return ResponseDto.databaseError();
        }

        return SignUpResponseDto.success();
    }

    @Override
    public ResponseEntity<? super SignInResponseDto> signIn(SignInRequestDto dto) {

        try {
            UserEntity userEntity = userRepository.findByEmail(dto.getEmail());
            if (userEntity == null || !passwordEncoder.matches(dto.getPassword(), userEntity.getPassword())) {
                return SignInResponseDto.signInFail();
            }

            Long userId = userEntity.getUserId();
            String role = userEntity.getRole();
            String nickName = userEntity.getNickname();
            String accessToken = jwtProvider.create(userId, role, nickName, 3600);
            String refreshToken = jwtProvider.create(userId, role, nickName,604800);

            // 레디스에 엑세스 토큰 저장
            redisService.setTokenData(accessToken, userId, role, nickName,3600);
            // 레디스에 리프레시 토큰 저장
            redisService.setTokenData(refreshToken, userId, role, nickName, 604800);

            // 로그 추가
            log.info("엑세스 토큰 저장: access:" + accessToken + " -> " + userId + role + nickName);
            log.info("리프레시 토큰 저장: refresh:" + refreshToken + " -> " + userId + role + nickName);

            return SignInResponseDto.success(accessToken, refreshToken);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseDto.databaseError();
        }
    }

    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response){
        String accessToken = TokenUtils.parseBearerToken(request);

        if (accessToken != null){
            Claims claims = jwtProvider.validate(accessToken);
            if (claims != null) {
                // Redis에서 Access Token 및 Refresh Token 삭제
                redisService.delete(accessToken);
                String refreshToken = TokenUtils.parseRefreshToken(request);
                if (refreshToken != null) {
                    redisService.delete(refreshToken);
                }

                // Refresh Token 쿠키 삭제
                Cookie refreshTokenCookie = new Cookie("refresh_token", null);
                refreshTokenCookie.setMaxAge(0);
                refreshTokenCookie.setPath("/");
                response.addCookie(refreshTokenCookie);

                // 응답 헤더에서 액세스 토큰 제거
                response.setHeader(HttpHeaders.AUTHORIZATION, "");
            } else {
                // 토큰이 유효하지 않은 경우 예외 처리
                throw new IllegalArgumentException("Invalid access token");
            }
        } else {
            // 토큰이 없는 경우 예외 처리
            throw new IllegalArgumentException("Access token is missing");
        }
    }
}
