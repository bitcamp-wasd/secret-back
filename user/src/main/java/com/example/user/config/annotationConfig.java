package com.example.user.config;

import com.example.user.annotation.HeaderUserAuthArgumentResolver;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
@RequiredArgsConstructor
public class annotationConfig implements WebMvcConfigurer {

    private final HeaderUserAuthArgumentResolver headerUserAuthArgumentResolver;

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(headerUserAuthArgumentResolver);
    }

}
