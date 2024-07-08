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

    @Column(name = "create_date", columnDefinition = "DATE DEFAULT 등록날짜")
    private LocalDate createDate=LocalDate.now();

    private Long userId;

    private Long battleId;

    private String comment;
}
