package com.example.video.global.annotation;

import com.example.video.dto.auth.UserAuth;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

@Component
@RequiredArgsConstructor
public class HeaderUserAuthArgumentResolver implements HandlerMethodArgumentResolver {

    private final ObjectMapper objectMapper;

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(HeaderUserAuth.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        HeaderUserAuth headerUserAuth = parameter.getParameterAnnotation(HeaderUserAuth.class);
        
        if(headerUserAuth != null) {
            String value = webRequest.getHeader(headerUserAuth.value());


            if(value != null) {
                return objectMapper.readValue(value, parameter.getParameterType());
            }
        }

        return null;
    }
}
