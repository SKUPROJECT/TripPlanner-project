package com.example.tripplanner.member.repository;

import com.example.tripplanner.member.entity.MemberEntity;
import com.example.tripplanner.member.memberEnum.Gender;
import com.example.tripplanner.member.memberEnum.Mbti;
import com.example.tripplanner.member.memberEnum.Role;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;

@SpringBootTest
class MemberRepositoryTest {
    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    public void testInsert(){
        for (int i = 1; i <= 10; i++) {
            MemberEntity memberEntity = MemberEntity.builder()
                    .id("user"+i+"@test.com")
                    .pw(passwordEncoder.encode("1234"))
                    .name("USER"+i)
                    .gender(Gender.MAIL)
                    .birth(LocalDateTime.now())
                    .mbti(Mbti.ENFJ)
                    .bio("테스트 계정 입니다.")
                    .role(i <= 5 ? Role.USER : Role.ADMIN)
                    .build();

            memberRepository.save(memberEntity);
        }
    }

}