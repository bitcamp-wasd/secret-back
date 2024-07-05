package com.example.user.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "user_rank")
@Table(name = "user_rank")
public class UserRankEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "rank_id")
    private Long rankId;

    @Column(nullable = false, unique = true)
    private String rankName;

    @Column(nullable = false)
    private int minPoint;

    @Column(nullable = false)
    private int maxPoint;

    private String imagePath;
    // 랭크 이미지 경로 이건 잠시 나중에 추가 방식 넣기

}
