package com.example.tripplanner.mypage.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class BaseResponse {

    int code;
    String message;

    @Builder
    public BaseResponse(int code, String message) {
        this.code = code;
        this.message = message;
    }
}