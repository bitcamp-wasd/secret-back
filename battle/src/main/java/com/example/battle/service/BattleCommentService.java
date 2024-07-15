package com.example.battle.service;

import com.example.battle.api.UserRestApi;
import com.example.battle.dto.CommentDto;
import com.example.battle.dto.user.response.UserRankInfoDto;
import com.example.battle.entity.Battle;
import com.example.battle.entity.BattleComment;
import com.example.battle.mapper.BattleMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Log4j2
public class BattleCommentService {

    private final BattleMapper battleMapper;
    private final UserRestApi userRestApi;

    public Slice<CommentDto> getComments(Long battleId, Pageable pageable) {
        int offset = (int)pageable.getOffset();
        int limit  = pageable.getPageSize();
        int total = battleMapper.countCommentsByBattleId(battleId);

        List<BattleComment> comments = battleMapper.getCommentsByBattleId(battleId, limit, offset);
        List<CommentDto> commentListDto = comments.stream()
                .map(comment -> {
                    UserRankInfoDto user = userRestApi.userRankInfo(comment.getUserId());
                    return new CommentDto(
                            comment.getBattleCommentId(),
                            user.getImagePath(),
                            user.getNickname(),
                            comment.getCreateDate(),
                            comment.getComment()
                    );
                })
                .collect(Collectors.toList());

        boolean hasNext = offset + limit < total;
        return new SliceImpl<>(commentListDto, pageable, hasNext);
    }

    public void addComment(Long battleId, Long userId, String comment) {
        BattleComment battleComment = new BattleComment();
        Battle battle = new Battle();
        battle.setBattleId(battleId);
        battleComment.setBattle(battle);
        battleComment.setUserId(userId);
        battleComment.setComment(comment);
        battleMapper.insertComment(battleComment);
    }

    public void updateComment(Long battleId, Long commentId, Long userId, String comment) {
        BattleComment battleComment = new BattleComment();
        Battle battle = new Battle();
        battle.setBattleId(battleId);
        battleComment.setBattleCommentId(commentId);
        battleComment.setBattle(battle);
        battleComment.setUserId(userId);
        battleComment.setComment(comment);
        battleMapper.updateComment(battleComment);
    }

    @Transactional
    public void deleteComment(Long battleId, Long commentId, Long userId) {
        log.info("Deleting comment with battleId: {}, commentId: {}, userId: {}", battleId, commentId, userId);
        int result = battleMapper.deleteComment(commentId, userId, battleId);
        if (result == 0) {
            throw new RuntimeException("삭제 실패");
        }
    }

    public int getCommentCountByBattleId(Long battleId){
        int count = battleMapper.countCommentsByBattleId(battleId);
        return count;
    }
}
