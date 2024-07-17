package com.example.battle.mapper;

import com.example.battle.dto.BattleDto;
import com.example.battle.dto.CommentDto;
import com.example.battle.entity.Battle;
import com.example.battle.entity.BattleComment;
import com.example.battle.entity.BattleVoteList;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;

@Mapper
public interface BattleMapper {

    List<Battle> findEndBattles(@Param("currentDate")LocalDateTime currentDate);

    void updateBattleState(@Param("battleId") Long battleId, @Param("state") String state);

    //배틀 등록
    void insertBattle(@Param("userId") Long userId,
                      @Param("battle") Battle battle);

    // 내 배틀 게시글
    List<Battle> findByMyBattle(@Param("userId") Long userId,
                                @Param("limit") int limit,
                                @Param("offset") int offset);

    int countByMyBattle(@Param("userId") Long userId);

    // 배틀 게시글
    void incrementViews(Long battleId);

    Battle getBattleById(@Param("battleId") Long battleId);

    List<Battle> findByStateBattle(@Param("state") String state,
                                   @Param("limit") int limit,
                                   @Param("offset") int offset);

    int countByState(@Param("state") String state);

    // 배틀 게시판 삭제 시 관련 데이터 삭제
    void deleteCommentsByBattleId(@Param("battleId") Long battleId);
    void deleteVotesByBattleId(@Param("battleId") Long battleId);
    void deleteBattleByBattleId(@Param("battleId") Long battleId);

    // 투표
    void insertBattleVote(@Param("battleId") Long battleId,
                          @Param("userId") Long userId,
                          @Param("postId") Long postId);

    void updateBattleVote(@Param("battleId") Long battleId,
                          @Param("userId") Long userId,
                          @Param("postId") Long postId);

    void deleteBattleVote(@Param("battleId") Long battleId,
                          @Param("userId") Long userId);

    Long getBattleVote(@Param("battleId") Long battleId,
                       @Param("userId") Long userId);

    void incrementVote1Cnt(@Param("battleId") Long battleId);

    void decrementVote1Cnt(@Param("battleId") Long battleId);

    void incrementVote2Cnt(@Param("battleId") Long battleId);

    void decrementVote2Cnt(@Param("battleId") Long battleId);

    // 댓글
    void insertComment(BattleComment battleComment);

    void updateComment(BattleComment battleComment);

    int deleteComment(@Param("battleCommentId") Long battleCommentId,
                       @Param("userId") Long userId,
                       @Param("battleId") Long battleId);

    int countCommentsByBattleId(@Param("battleId") Long battleId);

    List<BattleComment> getCommentsByBattleId(@Param("battleId") Long battleId,
                                              @Param("limit") int limit,
                                              @Param("offset") int offset);

}
