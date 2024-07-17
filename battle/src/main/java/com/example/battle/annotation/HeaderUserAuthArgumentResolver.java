package com.example.battle.annotation;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import lombok.extern.log4j.Log4j2;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import java.util.Base64;

@Component
@RequiredArgsConstructor
@Log4j2
public class HeaderUserAuthArgumentResolver implements HandlerMethodArgumentResolver {

    private final ObjectMapper objectMapper;
    private final Base64.Decoder decoder;

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        boolean supports = parameter.hasParameterAnnotation(HeaderUserAuth.class);
        log.info("supportsParameter called: {}", supports);
        return supports;
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        HeaderUserAuth headerUserAuth = parameter.getParameterAnnotation(HeaderUserAuth.class);

        if(headerUserAuth != null) {
            String value = webRequest.getHeader(headerUserAuth.value());
            log.info("Received header value: {}", value);


            if (value != null) {
                try {
                    String decodeValue = new String(decoder.decode(value));
                    log.info("Decoded header value: {}", decodeValue);
                    return objectMapper.readValue(decodeValue, parameter.getParameterType());
                } catch (Exception e) {
                    log.error("Failed to decode and parse header value", e);
                    throw e;
                }
            } else {
                log.warn("Header value is null");
            }
        }


        return null;
    }
}
