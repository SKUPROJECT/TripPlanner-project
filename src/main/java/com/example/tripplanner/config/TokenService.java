package com.example.tripplanner.config;

import com.example.tripplanner.member.entity.Member;
import com.example.tripplanner.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Duration;

@RequiredArgsConstructor
@Service
public class TokenService {
    private final TokenProvider tokenProvider;
    private final RefreshTokenService refreshTokenService;
    private final MemberService memberService;

    public String createNewAccessToken(String refreshToken) {
        System.out.println("Received refreshToken: " + refreshToken);

        if(!tokenProvider.validToken(refreshToken)) {
            System.err.println("Invalid refreshToken: " + refreshToken);

            throw new IllegalArgumentException("Unexpected refresh token");
        }

        Long memberId = refreshTokenService.findByRefreshToken(refreshToken).getMemberId();
        Member member = memberService.findById(memberId);

        return tokenProvider.generateToken(member, Duration.ofHours(2));
    }
}
