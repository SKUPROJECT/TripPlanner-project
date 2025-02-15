package com.example.tripplanner.member.service;

import com.example.tripplanner.member.dto.MemberDTO;
import com.example.tripplanner.member.dto.MemberInfoDTO;
import com.example.tripplanner.member.dto.TokenResponseDTO;
import com.example.tripplanner.member.entity.MemberEntity;
import com.example.tripplanner.member.exception.MemberExceptions;
import com.example.tripplanner.member.memberEnum.Auth;
import com.example.tripplanner.member.memberEnum.Gender;
import com.example.tripplanner.member.memberEnum.Mbti;
import com.example.tripplanner.member.memberEnum.Role;
import com.example.tripplanner.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Log4j2
@Transactional
public class MemberService {

    private final MemberRepository memberRepository;

    private final PasswordEncoder passwordEncoder;

    private final RestTemplate restTemplate;

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

    public MemberInfoDTO getMemberById(String id){
        Optional<MemberEntity> result = memberRepository.findById(id);
        MemberEntity memberEntity = result.orElseThrow(MemberExceptions.NOT_FOUND::get);

        return new MemberInfoDTO(memberEntity);
    }

    public MemberDTO googleLogin(String googleToken, String type){
        String url = "https://www.googleapis.com/oauth2/v3/userinfo";

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", type+" "+googleToken);

        HttpEntity<Map<String, String>> entity = new HttpEntity<>(headers);
        ResponseEntity<Map<String, String>> response = restTemplate.exchange(url, HttpMethod.GET, entity, new ParameterizedTypeReference<>() {});

        if (response.getStatusCode() == HttpStatus.OK) {
            Optional<MemberEntity> result = memberRepository.findById(Objects.requireNonNull(response.getBody()).get("email"));
            MemberEntity memberEntity;

            if (result.isPresent()) {
                memberEntity = result.get();
            } else {
                memberEntity = MemberEntity.builder()
                        .id(response.getBody().get("email"))
                        .pw(passwordEncoder.encode("1234"))
                        .name(response.getBody().get("name"))
                        .gender(Gender.MAIL)
                        .birth(LocalDate.now())
                        .mbti(Mbti.ENFJ)
                        .bio("테스트 계정 입니다.")
                        .role(Role.USER)
                        .auth(Auth.G)
                        .build();
                memberRepository.save(memberEntity);
            }

            return new MemberDTO(memberEntity);
        }else{
            throw MemberExceptions.NOT_FOUND.get();
        }
    }
}
