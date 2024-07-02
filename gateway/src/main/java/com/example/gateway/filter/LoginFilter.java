package com.example.gateway.filter;

import com.example.gateway.common.ResponseMessage;
import com.example.gateway.service.RedisService;
import com.example.gateway.util.JwtUtil;
import lombok.extern.log4j.Log4j2;
import org.apache.http.HttpHeaders;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;



@Component
@Log4j2
public class LoginFilter extends AbstractGatewayFilterFactory<LoginFilter.Config> {

    private final JwtUtil jwtUtil;
    private final RedisService redisService;

    public LoginFilter(JwtUtil jwtUtil, RedisService redisService) {
        super(Config.class);
        this.jwtUtil = jwtUtil;
        this.redisService = redisService;
    }



    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            ServerHttpRequest request = exchange.getRequest();
            ServerHttpResponse response = exchange.getResponse();

            // 로그인 필요 없는 기능
            if(!request.getPath().toString().contains("/auth"))
                return chain.filter(exchange);

            String authorization = jwtUtil.parseBearerToken(request);

            // 로그인이 안됬을 경우
            if(authorization == null)
                return jwtUtil.errorHandler(response, HttpStatus.UNAUTHORIZED, ResponseMessage.Login_Required);

            // access키가 존재하지 않을 경우 -> refresh token으로 재발급
            if(redisService.hasKey(authorization) == false)
                return jwtUtil.errorHandler(response, HttpStatus.BAD_REQUEST, ResponseMessage.Refresh_Token);


            ServerHttpRequest builder = request.mutate().header("user", redisService.get(authorization)).build();

            log.info(request.getHeaders().toSingleValueMap());

            // 통과
            return chain.filter(exchange.mutate().request(builder).build());
        };
    }

    public static class Config {
        // Put the configuration properties
    }
}
