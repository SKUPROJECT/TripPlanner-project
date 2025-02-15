package com.example.tripplanner.member.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name="tbl_members_follows")
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FollowEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name="follower_id")
    private MemberEntity follwers;

    @ManyToOne
    @JoinColumn(name = "following_id")
    private MemberEntity following;  // 팔로잉
}
