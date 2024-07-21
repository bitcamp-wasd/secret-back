package com.example.battle.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BattleMyCommentDto {
    private Long battleCommentId;
    private String comment;
    private LocalDateTime createDate;
    private Long battleId;
}
