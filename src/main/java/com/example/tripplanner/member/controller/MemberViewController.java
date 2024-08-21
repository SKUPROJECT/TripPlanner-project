package com.example.tripplanner.member.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MemberViewController {
    @GetMapping("/login")
    public String memberLogin() {
        return "login";
    }

    @GetMapping("/signup")
    public String memebrSignup() {
        return "signup";
    }
}
