package com.example.tripplanner.member.dto;

import com.example.tripplanner.member.entity.MemberEntity;
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
    private String email;
    private LocalDateTime joinDate;
    private LocalDateTime modifiedDate;
    private String role;

    /* 토큰발행에 필요한 항목 */
    public Map<String, Object> getDataMap(){
        Map<String, Object> map = new HashMap<>();
        map.put("id", id);
        map.put("name", name);
        map.put("email", email);
        map.put("role", role);
        return map;
    }

    public MemberDTO(MemberEntity memberEntity){
        this.id = memberEntity.getId();
        this.pw = memberEntity.getPw();
        this.name = memberEntity.getName();
        this.email = memberEntity.getEmail();
        this.joinDate = memberEntity.getJoinDate();
        this.modifiedDate = memberEntity.getModifiedDate();
        this.role = memberEntity.getRole();
    }
}
