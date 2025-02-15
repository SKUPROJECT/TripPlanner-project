package com.example.tripplanner.member.dto;

import com.example.tripplanner.member.entity.MemberEntity;
import com.example.tripplanner.member.memberEnum.Gender;
import com.example.tripplanner.member.memberEnum.Mbti;
import com.example.tripplanner.member.memberEnum.Role;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Schema(description = "프로필 정보 Response")
public class MemberInfoDTO {

    @Schema(name = "id", description = "사용자 아이디 (이메일)", example = "user1@test.com")
    private String id;

    @Schema(name = "name", description = "사용자 이름", example = "USER1")
    private String name;

    @Schema(name = "gender", description = "성별", example = "MAIL")
    private Gender gender;
    
    @Schema(name = "birth", description = "생년월일", example = "2025-02-15")
    private LocalDate birth;

    @Schema(name = "mbti", description = "사용자 MBTI", example = "ISFJ")
    private Mbti mbti;

    @Schema(name = "profileImgUrl", description = "프로필 사진", example = "/user/profile.img", nullable = true)
    private String profileImgUrl;

    @Schema(name = "bio", description = "자기소개", example = "테스트 계정 입니다.", nullable = true)
    private String bio;

    public MemberInfoDTO(MemberEntity memberEntity){
        this.id = memberEntity.getId();
        this.name = memberEntity.getName();
        this.birth = memberEntity.getBirth();
        this.gender = memberEntity.getGender();
        this.mbti = memberEntity.getMbti();
        this.profileImgUrl = memberEntity.getProfileImageUrl();
        this.bio = memberEntity.getBio();
    }
}
