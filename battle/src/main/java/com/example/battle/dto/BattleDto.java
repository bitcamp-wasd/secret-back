package com.example.battle.dto;

import com.example.battle.dto.info.PostDto;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class BattleDto {
    private Long battleId;
    private String title;
    private Long views;

//    private Long views;

    private LocalDate endDate;
    private Long vote1Cnt;
    private Long vote2Cnt;

    // 나중 api 형식으로 변환
    private PostDto postId1;
    private PostDto postId2;

}
