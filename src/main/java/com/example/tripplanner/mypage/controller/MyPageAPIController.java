package com.example.tripplanner.mypage.controller;

import com.example.tripplanner.mypage.dto.BaseResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class MyPageAPIController {

    @PostMapping("/api/memberInfo")
    @Operation(summary = "내정보 조회", description = "회원정보를 조회하는 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "2000", description = "요청성공", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "4001", description = "요청실패, 필수 파라미터 누락", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "4002", description = "요청실패, 유저 정보 없음", content = @Content(mediaType = "application/json"))
    })
    @Parameters({
            @Parameter(name = "email", description = "이메일", example = "4sunskyhyun@gmail.com")
    })
    public BaseResponse memberInfo(String email){
        return null;
    }

    @PostMapping("/api/memberUpdate")
    @Operation(summary = "내정보 수정", description = "회원정보를 수정하는 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "2000", description = "요청성공", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "4001", description = "요청실패, 필수 파라미터 누락", content = @Content(mediaType = "application/json")),
    })
    @Parameters({
            @Parameter(name = "email", description = "이메일", example = "4sunskyhyun@gmail.com")
    })
    public BaseResponse memberUpdate(String email){

        return null;
    }
}
