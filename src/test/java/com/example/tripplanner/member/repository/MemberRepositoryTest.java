package com.example.tripplanner.member.repository;

import com.example.tripplanner.member.entity.MemberEntity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class MemberRepositoryTest {
    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    public void testInsert(){
        for (int i = 1; i <= 100; i++) {
            MemberEntity memberEntity = MemberEntity.builder()
                    .mid("user"+i)
                    .mpw(passwordEncoder.encode("1111"))
                    .mname("USER"+i)
                    .email("user"+i+"@aaa.com")
                    .role(i <= 80 ? "USER" : "ADMIN")
                    .build();

            memberRepository.save(memberEntity);
        }
    }

}