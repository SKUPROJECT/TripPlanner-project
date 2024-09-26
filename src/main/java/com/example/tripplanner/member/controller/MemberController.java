package com.example.tripplanner.member.controller;

import com.example.tripplanner.member.dto.MemberDTO;
import com.example.tripplanner.member.security.util.JWTUtil;
import com.example.tripplanner.member.service.MemberService;
import io.jsonwebtoken.ExpiredJwtException;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/member")
@Log4j2
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    private final JWTUtil jwtUtil;

    @PostMapping("/token")
    public ResponseEntity<Map<String, String>> makeToken(@RequestBody MemberDTO memberDTO){
        log.info("make token .........");
        MemberDTO memberDTOResult = memberService.read(memberDTO.getMid(), memberDTO.getMpw());

        log.info(memberDTOResult);

        String mid = memberDTO.getMid();
        Map<String, Object> dataMap = memberDTOResult.getDataMap();
        String accessToken = jwtUtil.createToken(dataMap, 60);
        String refreshToken = jwtUtil.createToken(Map.of("mid", mid), 60 * 24 * 7);

        log.info("accessToken : "+accessToken);
        log.info("refreshToken : "+refreshToken);

        return ResponseEntity.ok(Map.of("accessToken", accessToken, "refreshToken", refreshToken));
    }

    @PostMapping("/refresh")
    public ResponseEntity<Map<String, String>> refreshToken(
            @RequestHeader("Authorization") String accessTokenStr,
            @RequestParam("refreshToken") String refreshToken,
            @RequestParam("mid") String mid
    ){

        log.info("access token with Bearer........." + accessTokenStr);
        if(accessTokenStr == null || !accessTokenStr.startsWith("Bearer ")){
            return handleException("No Access Token", 400);
        }

        if(refreshToken == null){
            return handleException("No Refresh Token", 400);
        }

        log.info("refresh token ......."+refreshToken);

        if(mid == null){
            return handleException("No Mid", 400);
        }

        String accessToken = accessTokenStr.substring(7);

        try{
            jwtUtil.validateToken(accessToken);
            Map<String, String> data = makeData(mid, accessToken, refreshToken);

            log.info("Access Token is not expired ...........");

            return ResponseEntity.ok(data);
        } catch(ExpiredJwtException expiredJwtException){ /* 토큰이 만료될 시 */
            try{
                Map<String, String> newTokenMap = makeNewToken(mid, refreshToken);
                return ResponseEntity.ok(newTokenMap);
            } catch(Exception e){
                return handleException("REFRESH "+e.getMessage(), 400);
            }
        }catch(Exception e){
            e.printStackTrace();
            return handleException(e.getMessage(), 400);
        }
    }

    private ResponseEntity<Map<String, String>> handleException(String msg, int status){
        return ResponseEntity.status(status).body(Map.of("error", msg));
    }

    private Map<String, String> makeData(String mid, String accessToken, String refreshToken){
        return Map.of("mid", mid, accessToken, refreshToken);
    }

    private Map<String, String> makeNewToken(String mid, String refreshToken){
        Map<String, Object> claims = jwtUtil.validateToken(refreshToken);

        log.info("refresh token claims: " + claims);
        if(!mid.equals(claims.get("mid").toString())){
            throw new RuntimeException("Invalid Refresh Token Host");
        }

        MemberDTO memberDTO = memberService.getByMid(mid);
        Map<String, Object> newClaims = memberDTO.getDataMap();
        String newAccessToken = jwtUtil.createToken(newClaims, 10);
        String newRefreshToken = jwtUtil.createToken(Map.of("mid", mid), 60 * 24 * 7);

        return makeData(mid, newAccessToken, newRefreshToken);
    }
}
