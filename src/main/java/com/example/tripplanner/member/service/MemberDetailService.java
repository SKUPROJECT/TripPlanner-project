package com.example.tripplanner.member.service;

import com.example.tripplanner.member.entity.Member;
import com.example.tripplanner.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class MemberDetailService implements UserDetailsService {

    private final MemberRepository memberRepository;

    //email로 사용자 정보를 가져오는 메서드
    @Override
    public Member loadUserByUsername(String email){
        return memberRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException(email));
    }


}
