package com.example.battle.entity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import java.time.LocalDate;

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

    @Column(name = "post_id1")
    private Long postId1;

    @Column(name = "post_id2")
    private Long postId2;

    @Column(name = "user_id")
    private Long userId;

    private String title;

    @Column(name = "create_date")
    private LocalDate createDate = LocalDate.now();

    @Column(name = "end_date")
    private LocalDate endDate = LocalDate.now().plusWeeks(1);

    private String state = "진행중";

    @ColumnDefault("0")
    @Column(name = "vote1_cnt")
    private int vote1Cnt;

    @ColumnDefault("0")
    @Column(name = "vote2_cnt")
    private int vote2Cnt;

    @ColumnDefault("0")
    @Column(name = "views")
    private int views;

}
