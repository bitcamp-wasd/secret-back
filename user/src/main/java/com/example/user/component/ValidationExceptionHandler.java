package com.example.user.component;

import com.example.user.dto.response.ResponseDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ValidationExceptionHandler {

    // @ExceptionHandler는 특정 예외 클래스 또는 그 서브 클래스에서 발생하는 예외를 처리하는 메서드를 지정하는 데 사용
    // MethodArgumentNotValidException: 주로 @Valid 어노테이션이 붙은 객체의 유효성 검사에서 실패할 경우 발생
    // HttpMessageNotReadableException: HTTP 메시지가 올바르지 않거나 파싱할 수 없는 경우 발생
    @ExceptionHandler({MethodArgumentNotValidException.class, HttpMessageNotReadableException.class})
    public ResponseEntity<ResponseDto> validationExceptionHandler(Exception exception) {
        return ResponseDto.validationFail();
    }

    @ExceptionHandler(OAuth2AuthenticationException.class)
    public ResponseEntity<String> handleOAuth2AuthenticationException(OAuth2AuthenticationException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }
}
