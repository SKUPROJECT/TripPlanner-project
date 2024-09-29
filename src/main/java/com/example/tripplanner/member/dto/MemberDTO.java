package com.example.tripplanner.member.dto;

import com.example.tripplanner.member.entity.MemberEntity;
import com.example.tripplanner.member.memberEnum.Gender;
import com.example.tripplanner.member.memberEnum.Mbti;
import com.example.tripplanner.member.memberEnum.Role;
import lombok.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class MemberDTO {

    private String id;
    private String pw;
    private String name;
    private Gender gender;
    private LocalDateTime birth;
    private Mbti mbti;
    private String bio;
    private LocalDateTime joinDate;
    private LocalDateTime modifiedDate;
    private Role role;

    public Map<String, Object> getDataMap(){
        Map<String, Object> map = new HashMap<>();
        map.put("id", id);
        map.put("name", name);
        map.put("role", role);
        return map;
    }

    public MemberDTO(MemberEntity memberEntity){
        this.id = memberEntity.getId();
        this.pw = memberEntity.getPw();
        this.name = memberEntity.getName();
        this.birth = memberEntity.getBirth();
        this.mbti = memberEntity.getMbti();
        this.bio = memberEntity.getBio();
        this.joinDate = memberEntity.getJoinDate();
        this.modifiedDate = memberEntity.getModifiedDate();
        this.role = memberEntity.getRole();
    }
}
