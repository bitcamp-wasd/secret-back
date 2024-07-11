package com.example.battle.service;

import com.example.battle.dto.CommentDto;
import com.example.battle.dto.info.UserDto;
import com.example.battle.entity.Battle;
import com.example.battle.entity.BattleComment;
import com.example.battle.mapper.BattleMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
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

    private List<UserDto> mockUserData() {
        // 임의의 유저 데이터를 생성합니다.
        return List.of(
                new UserDto(1L, "user1", "/images/user1.png"),
                new UserDto(2L, "user2", "/images/user2.png")
        );
    }

    private UserDto getUserInfo(Long userId) {
        // 임의의 유저 데이터를 가져옵니다.
        return mockUserData().stream()
                .filter(user -> user.getUserId().equals(userId))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    public Page<CommentDto> getComments(Long battleId, Pageable pageable) {
        List<BattleComment> comments = battleMapper.getCommentsByBattleId(battleId, pageable);
        List<CommentDto> commentListDto = comments.stream()
                .map(comment -> {
                    UserDto user = getUserInfo(comment.getUserId());
                    return new CommentDto(
                            comment.getBattleCommentId(),
                            user.getImagePath(),
                            user.getNickname(),
                            comment.getCreateDate(),
                            comment.getComment()
                    );
                })
                .collect(Collectors.toList());
        int total = battleMapper.countCommentsByBattleId(battleId);
        return new PageImpl<>(commentListDto, pageable, total);
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
