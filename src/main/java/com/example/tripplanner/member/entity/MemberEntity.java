package com.example.tripplanner.member.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
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
    private String id;

    private String pw;

    private String name;

    private String email;

    @CreatedDate
    private LocalDateTime joinDate;

    @LastModifiedDate
    private LocalDateTime modifiedDate;

    private String role;
}
