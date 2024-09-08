package com.example.tripplanner.member.controller;

import com.example.tripplanner.member.dto.AddMemberRequest;
import com.example.tripplanner.member.dto.AddMemberResponse;
import com.example.tripplanner.member.service.MemberService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;

@RequiredArgsConstructor
@Controller
public class MemberApiController {
    private final MemberService memberService;

    @PostMapping("/api/user")
    @ResponseBody
    public AddMemberResponse signup(AddMemberRequest request){
        memberService.save(request);
        AddMemberResponse response = new AddMemberResponse();

        return response ;
    }

//    public HashMap<String, String> signup(AddMemberRequest request){
//        memberService.save(request);
//        HashMap<String,String> map = new HashMap<>();
//        map.put("a","a");
//        return map;
//    } 데이터로 처리하는 방식

    @GetMapping("/api/logout")
    public String logout(HttpServletRequest request, HttpServletResponse response){
        new SecurityContextLogoutHandler().logout(request, response,
                SecurityContextHolder.getContext().getAuthentication());
        return "redirect:/login";
    }
}
