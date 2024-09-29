package com.example.tripplanner.member.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Schema(description = "토큰정보 Response")
@Getter
public class TokenResponseDTO {

    @Schema(name = "accessToken", description = "로그인 이후 인증을 받기 위해 사용하는 토큰", example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...")
    String accessToken;

    @Schema(name = "refreshToken", description = "액세스 토큰이 만료된 후 새로운 액세스 토큰을 발급받기 위해 사용하는 토큰", example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...")
    String refreshToken;
}

