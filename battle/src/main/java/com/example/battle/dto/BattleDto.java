package com.example.battle.dto;

import com.example.battle.dto.post.response.PostInfoDto;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
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
    
    private Long userId;

    private LocalDateTime endDate;
    private Long vote1Cnt;
    private Long vote2Cnt;

    // 나중 api 형식으로 변환
    private PostInfoDto postId1;
    private PostInfoDto postId2;

}
