package com.example.battle.dto;

import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CommentDto {

    private Long battleCommentId;

    // userId에 저장되어있는 랭크 이미지
    private String rankname;

    // userId에 저장되어있는 닉네임
    private String nickname;

    private LocalDateTime createDate;
    private String comment;
}
