package com.example.battle.dto.battle.request;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class BattleCommentListDto {
    private List<Long> battleCommentId;
}