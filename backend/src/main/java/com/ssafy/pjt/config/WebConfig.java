package com.ssafy.pjt.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig {
	
	@Value("${user.home}")
	private String userHome;
	
    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
        	
        	// ✅ 1. CORS 설정
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/ai/**") // or "/**" for all endpoints
                        .allowedOrigins("http://localhost:5173")
                        .allowedMethods("GET", "POST", "PUT", "DELETE")
                        .allowedHeaders("*")
                        .allowCredentials(true);
            }
            
            // ✅ 2. 로컬 이미지 경로를 URL로 매핑
            @Override
            public void addResourceHandlers(ResourceHandlerRegistry registry) {
                // 실제 저장 위치: ${user.home}/uploads/profile/
                String uploadDir = "file:" + userHome + "/uploads/profile/";

                registry.addResourceHandler("/uploads/profile/**") // 클라이언트 요청 경로
                        .addResourceLocations(uploadDir) // 실제 파일 위치
                        .setCachePeriod(3600); // 1시간 캐시 (선택)
            }
        };
    }
}
