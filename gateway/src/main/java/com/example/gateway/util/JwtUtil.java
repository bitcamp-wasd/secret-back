package com.example.gateway.util;


import com.example.gateway.common.ResponseMessage;
import org.apache.http.HttpHeaders;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;


import java.nio.charset.StandardCharsets;

@Component
public class JwtUtil {

    public DataBuffer errorMessage(ServerHttpResponse response, String message) {
        String errorJson = String.format("{\"errorMessage\": \"%s\"}", message);
        byte[] bytes = errorJson.getBytes(StandardCharsets.UTF_8);
        DataBuffer buffer = response.bufferFactory().wrap(bytes);
        return buffer;
    }

    /**
     * 필터 에러 핸들러
     * @param response 요청 반환 값
     * @param status 에러 코드
     * @param message 에러 메시지 반환 값
     * @return
     */
    public Mono<Void> errorHandler(ServerHttpResponse response, HttpStatus status, String message) {
        return Mono.defer(() -> {
            response.setStatusCode(status);
            response.getHeaders().add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
            DataBuffer buffer = errorMessage(response, message);
            return response.writeWith(Mono.just(buffer)).doOnError(error -> DataBufferUtils.release(buffer));
        });
    }
}
