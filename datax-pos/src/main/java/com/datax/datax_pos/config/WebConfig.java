package com.datax.datax_pos.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        // Esto permite que tu frontend (ej. pos_cajero.html)
        // haga cualquier tipo de petición (GET, POST, PUT, etc.)
        // a tu API de Spring Boot.
        registry.addMapping("/api/**")  // Permite todo bajo /api/
                .allowedOrigins("*")       // Permite cualquier origen (para desarrollo)
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("*");
    }
}