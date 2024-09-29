package com.example.tripplanner.member.entity;

import com.example.tripplanner.member.memberEnum.Gender;
import com.example.tripplanner.member.memberEnum.Mbti;
import com.example.tripplanner.member.memberEnum.Role;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Table(name="tbl_members")
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EntityListeners(value={AuditingEntityListener.class})
public class MemberEntity {

    @Id
    @Column(name = "member_id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long member_Id;

    @Column(name = "id", nullable = false, length = 256, unique = true)
    private String id;

    // BCrypt로 인코딩된 비밀번호는 항상 60자
    @Column(name = "pw", nullable = false, length = 60)
    private String pw;

    @Column(name = "name", nullable = false, length = 40)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(name = "gender", nullable = false, length = 10)
    private Gender gender;

    @Column(name = "birth", nullable = false)
    private LocalDateTime birth;

    @Enumerated(EnumType.STRING)
    @Column(name = "mbti", nullable = false, length = 4)
    private Mbti mbti;

    @Column(name = "bio", length = 100)
    private String bio;

    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false, length = 20)
    private Role role;

    @CreatedDate
    @Column(name = "joinDate", updatable = false)
    private LocalDateTime joinDate;

    @LastModifiedDate
    @Column(name = "modifiedDate")
    private LocalDateTime modifiedDate;
}
