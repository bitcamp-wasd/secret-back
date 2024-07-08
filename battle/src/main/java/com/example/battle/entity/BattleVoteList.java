package com.example.battle.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class BattleVoteList {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long battleVoteId;

    private Long battleId;

    private Long userId;

    private Long postId;

}
