package com.example.tripplanner.member.controller;

import com.example.tripplanner.member.dto.MemberDTO;
import com.example.tripplanner.member.dto.TokenResponseDTO;
import com.example.tripplanner.security.util.JWTUtil;
import com.example.tripplanner.member.service.MemberService;
import io.jsonwebtoken.ExpiredJwtException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.headers.Header;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/member")
@Tag(name = "사용자 관리", description = "사용자와 관련된 API를 관리합니다.")
@Log4j2
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    private final JWTUtil jwtUtil;

    @PostMapping("/token")
    @Operation(summary = "로그인 토큰발급 API", description = "사용자의 아이디와 비밀번호를 전달받아 일치 시 사용자에게 토큰정보를 전달합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "로그인 성공", content = @Content(mediaType = "application/json",schema = @Schema(implementation = TokenResponseDTO.class))),
            @ApiResponse(responseCode = "404", description = "사용자 정보 없음", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "401", description = "사용자 정보 불일치", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "400", description = "필수 파라미터 누락", content = @Content(mediaType = "application/json"))
    })
    public ResponseEntity<Map<String, String>> makeToken(
            @Parameter(name = "id", description = "사용자 아이디", required=true)
            @RequestParam("id") String id,
            @Parameter(name = "pw", description = "사용자 비밀번호", required=true)
            @RequestParam("pw") String pw){

        log.info("make token .........");
        MemberDTO memberDTOResult = memberService.read(id, pw);

        log.info(memberDTOResult);

        Map<String, Object> dataMap = memberDTOResult.getDataMap();
        String accessToken = jwtUtil.createToken(dataMap, 10);
        String refreshToken = jwtUtil.createToken(Map.of("id", memberDTOResult.getId()), 60 * 24 * 7);

        log.info("accessToken : "+accessToken);
        log.info("refreshToken : "+refreshToken);

        return ResponseEntity.ok(makeData(accessToken, refreshToken));
    }

    @PostMapping("/refresh")
    @Operation(summary = "로그인 토큰 재발급 API",
            description = "토큰정보 만료 시 헤더에 액세스 토큰과 바디에 리프레시 토큰정보를 전달받아 액세스 토큰을 재발급 합니다.<br><br>" +
                    "** 만료일자가 지나지 않은 액세스 토큰 요청 시에는 요청한 토큰 정보를 반환합니다."
            ,security = @SecurityRequirement(name = "Authorization"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "토큰 재발급 성공", content = @Content(mediaType = "application/json",schema = @Schema(implementation = TokenResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "필수 파라미터 누락 또는 유효하지 않은 토큰정보", content = @Content(mediaType = "application/json")),
    })
    public ResponseEntity<Map<String, String>> refreshToken(
            @Parameter(hidden = true) @RequestHeader("Authorization") String accessTokenStr,
            @Parameter(description = "리프레시 토큰", required = true)
            @RequestParam("refreshToken") String refreshToken,
            @Parameter(description = "사용자 아이디", required = true)
            @RequestParam("id") String id
    ){

        log.info("access token with Bearer........." + accessTokenStr);
        if(accessTokenStr == null || !accessTokenStr.startsWith("Bearer ")){
            return handleException("No Access Token", 400);
        }

        if(refreshToken == null){
            return handleException("No Refresh Token", 400);
        }

        log.info("refresh token ......."+refreshToken);

        if(id == null){
            return handleException("No id", 400);
        }

        String accessToken = accessTokenStr.substring(7);

        try{
            jwtUtil.validateToken(accessToken);
            Map<String, String> data = makeData(accessToken, refreshToken);

            log.info("Access Token is not expired ...........");

            return ResponseEntity.ok(data);
        } catch(ExpiredJwtException expiredJwtException){ /* 토큰 만료를 확인하고 재발급 */
            try{
                Map<String, String> newTokenMap = makeNewToken(id, refreshToken);
                return ResponseEntity.ok(newTokenMap);
            } catch(Exception e){
                return handleException("REFRESH "+e.getMessage(), 400);
            }
        }catch(Exception e){
            return handleException(e.getMessage(), 400);
        }
    }

    private ResponseEntity<Map<String, String>> handleException(String msg, int status){
        return ResponseEntity.status(status).body(Map.of("error", msg));
    }

    private Map<String, String> makeData(String accessToken, String refreshToken){
        return Map.of("accessToken", accessToken, "refreshToken" , refreshToken);
    }

    private Map<String, String> makeNewToken(String id, String refreshToken){
        Map<String, Object> claims = jwtUtil.validateToken(refreshToken);

        log.info("refresh token claims: " + claims);
        if(!id.equals(claims.get("id").toString())){
            throw new RuntimeException("Invalid Refresh Token Host");
        }

        MemberDTO memberDTO = memberService.getById(id);
        Map<String, Object> newClaims = memberDTO.getDataMap();
        String newAccessToken = jwtUtil.createToken(newClaims, 10);
        String newRefreshToken = jwtUtil.createToken(Map.of("id", id), 60 * 24 * 7);

        return makeData(newAccessToken, newRefreshToken);
    }
}
