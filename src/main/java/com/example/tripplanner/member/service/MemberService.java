package com.example.tripplanner.member.service;

import com.example.tripplanner.member.dto.MemberDTO;
import com.example.tripplanner.member.entity.MemberEntity;
import com.example.tripplanner.member.exception.MemberExceptions;
import com.example.tripplanner.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Log4j2
@Transactional
public class MemberService {

    private final MemberRepository memberRepository;

    private final PasswordEncoder passwordEncoder;

    public MemberDTO read(String id, String pw){
        Optional<MemberEntity> result = memberRepository.findById(id);
        MemberEntity memberEntity = result.orElseThrow(MemberExceptions.NOT_FOUND::get);

        if(!passwordEncoder.matches(pw, memberEntity.getPw())){
            throw MemberExceptions.BAD_CREDENTIALS.get();
        }

        return new MemberDTO(memberEntity);
    }

    public MemberDTO getById(String id){
        Optional<MemberEntity> result = memberRepository.findById(id);
        MemberEntity memberEntity = result.orElseThrow(MemberExceptions.NOT_FOUND::get);

        return new MemberDTO(memberEntity);
    }
}
