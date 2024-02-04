package com.psj.itembrowser.security.common.config;

import com.psj.itembrowser.security.common.convertor.YearStringToLocalDateConverter;
import javax.validation.constraints.NotNull;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {
    
    private final YearStringToLocalDateConverter yearStringToLocalDateConverter;
    
    @Override
    public void addFormatters(@NonNull FormatterRegistry registry) {
        registry.addConverter(yearStringToLocalDateConverter);
    }
    
    @Override
    public void addResourceHandlers(@NotNull ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/static/**").addResourceLocations("classpath:/static/");
        registry.addResourceHandler("swagger-ui.html")
            .addResourceLocations("classpath:/static/swagger-ui/");
    }
}