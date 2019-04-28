package org.ebook.config;

import org.ebook.interceptor.*;
import org.springframework.context.annotation.*;
import org.springframework.web.servlet.config.annotation.*;

@Configuration
public class InterceptorConfiguration implements WebMvcConfigurer {
    @Bean
    public AuthInterceptor getAuthInterceptor() {
        return new AuthInterceptor();
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(getAuthInterceptor())
                .addPathPatterns("/users/**")
                .addPathPatterns("/cart/**")
                .addPathPatterns("/order/**")
                .addPathPatterns("/management/**")
                .addPathPatterns("/login/**")
                .addPathPatterns("/register/**");
    }
}
