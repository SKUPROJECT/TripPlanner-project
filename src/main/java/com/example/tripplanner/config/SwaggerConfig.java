package com.example.tripplanner.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {
    @Bean
    public OpenAPI openAPI() {
        // Swagger에서 제공하는 OpenAPI 객체를 생성하고 반환
        return new OpenAPI()
                .components(new Components()
                .addSecuritySchemes("Authorization", new SecurityScheme() /* SWAGGER UI 헤더 설정 */
                        .name("Authorization")
                        .type(SecurityScheme.Type.APIKEY)
                        .in(SecurityScheme.In.HEADER)
                        .bearerFormat("JWT")
                ))
                .info(apiInfo());
    }

    private Info apiInfo() {
        return new Info()
                .title("Trip Planner API")
                .description("여행지 추천 프로젝트 API")
                .version("1.0.0");
    }
}