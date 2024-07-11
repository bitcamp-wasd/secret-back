package com.example.battle.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class BattleComment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long battleCommentId;

    @Column(name = "create_date")
    private LocalDate createDate=LocalDate.now();

    // api 적용시 조인 제거
    @Column(name = "user_id")
    private Long userId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "battle_id")
    private Battle battle;

    private String comment;
}
