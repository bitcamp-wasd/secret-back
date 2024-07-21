package com.example.user.dto.battle.response;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class BattleMyCommentDto {

    private Long battleCommentId;
    private String comment;
    private LocalDateTime createDate;
    private Long battleId;
}
