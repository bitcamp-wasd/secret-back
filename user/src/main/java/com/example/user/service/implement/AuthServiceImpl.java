package com.example.user.service.implement;

import com.example.user.common.CertificationNumber;
import com.example.user.dto.request.auth.*;
import com.example.user.dto.response.ResponseDto;
import com.example.user.dto.response.auth.*;
import com.example.user.entity.UserEntity;
import com.example.user.filter.TokenUtils;
import com.example.user.provider.EmailProvider;
import com.example.user.provider.JwtProvider;
import com.example.user.repository.UserRepository;
import com.example.user.service.AuthService;
import com.example.user.service.RedisService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private static final Logger logger = LoggerFactory.getLogger(AuthServiceImpl.class);

    private final UserRepository userRepository;
    private final RedisService redisService;
    private final JwtProvider jwtProvider;
    private final EmailProvider emailProvider;

    private PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Override
    public ResponseEntity<? super NickNameCheckResponseDto> nickNameCheck(NickNameCheckRequestDto dto) {

        try {

            String nickName = dto.getNickName();

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

            boolean isExistNickName = userRepository.existsByNickname(nickName);
            if (isExistNickName)
                return NickNameCheckResponseDto.duplicateNickName();

            UserEntity userEntity = new UserEntity(dto);
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

            String email = userEntity.getEmail();
            String accessToken = jwtProvider.create(email, 3600);
            String refreshToken = jwtProvider.create(email, 604800);

            // 레디스에 엑세스 토큰 저장
            redisService.set("access:" + email, accessToken, 3600);
            // 레디스에 리프레시 토큰 저장
            redisService.set("refresh:" + email, refreshToken, 604800);

            // 로그 추가
            System.out.println("엑세스 토큰 저장: access:" + email + " -> " + accessToken);
            System.out.println("리프레시 토큰 저장: refresh:" + email + " -> " + refreshToken);

            return SignInResponseDto.success(accessToken, refreshToken);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseDto.databaseError();
        }
    }

    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response){
        String token = TokenUtils.parseBearerToken(request);

        if (token != null){
            String email = jwtProvider.validate(token);
            if (email != null) {
                // Redis에서 Refresh Token 삭제
                redisService.delete("access:" + email);
                redisService.delete( "refresh:" + email);

                // Access Token 쿠키 삭제
                Cookie cookie = new Cookie("access_token", null);
                cookie.setMaxAge(0);
                cookie.setPath("/");
                response.addCookie(cookie);

                // 응답 헤더에서 액세스 토큰 제거
                response.setHeader(HttpHeaders.AUTHORIZATION, "");
            }
        }
    }
}
