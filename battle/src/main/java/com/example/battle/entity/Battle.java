package com.example.battle.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "battle")
public class Battle {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long battleId;

    private Long postId1;

    private Long postId2;

    private Long userId;

    private String title;

    @Column(name = "create_date", columnDefinition = "DATE DEFAULT 등록날짜")
    private LocalDate createDate = LocalDate.now();

    @Column(name = "end_date", columnDefinition = "DATE DEFAULT 등록날짜 + INTERVAL 7 DAY")
    private LocalDate endDate = LocalDate.now().plusWeeks(1);

    @Column(columnDefinition = "DEFAULT '진행중'")
    private String state = "진행중";

    @Column(columnDefinition = "투표수 DEFAULT 0")
    private Long vote1Cnt;

    @Column(columnDefinition = "투표수 DEFAULT 0")
    private Long vote2Cnt;

    @OneToMany(mappedBy = "battleId", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<BattleVoteList> voteList = new ArrayList<>();

    @OneToMany(mappedBy = "battleId", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<BattleComment> comments = new ArrayList<>();

}
