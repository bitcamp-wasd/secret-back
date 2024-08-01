package com.example.battle.service;

import com.example.battle.entity.Battle;
import com.example.battle.mapper.BattleMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Log4j2
public class BattleVoteService {

    private final BattleMapper battleMapper;

    @Transactional
    public void vote(Long battleId, Long userId, Long postId) {
        // 기존 투표 확인
        Long existingVotePostId = battleMapper.getBattleVote(battleId, userId);
        log.info("existingVotePostId 투표확인: {}", existingVotePostId);

        Battle battle = battleMapper.getBattleById(battleId);
        log.info("battle: {}", battle);

        if (existingVotePostId != null) {
            // 기존에 투표한 경우
            if (existingVotePostId.equals(postId)) {
                // 같은 게시물에 다시 투표한 경우 투표 삭제
                battleMapper.deleteBattleVote(battleId, userId);
                log.info("기존 투표 삭제됨.");

                if (postId.equals(battle.getPostId1())) {
                    battleMapper.decrementVote1Cnt(battleId);
                    log.info("vote1 감소. battleId: {}", battleId);
                } else if (postId.equals(battle.getPostId2())) {
                    battleMapper.decrementVote2Cnt(battleId);
                    log.info("vote2 감소. battleId: {}", battleId);
                }
            } else {
                // 다른 게시물에 투표한 경우 투표 수정
                if (existingVotePostId.equals(battle.getPostId1())) {
                    battleMapper.decrementVote1Cnt(battleId);
                    log.info("vote1 감소. battleId: {}", battleId);
                } else if (existingVotePostId.equals(battle.getPostId2())) {
                    battleMapper.decrementVote2Cnt(battleId);
                    log.info("vote2 감소. battleId: {}", battleId);
                }

                battleMapper.updateBattleVote(battleId, userId, postId);
                log.info("기존 투표 수정됨.");

                if (postId.equals(battle.getPostId1())) {
                    battleMapper.incrementVote1Cnt(battleId);
                    log.info("vote1 증가. battleId: {}", battleId);
                } else if (postId.equals(battle.getPostId2())) {
                    battleMapper.incrementVote2Cnt(battleId);
                    log.info("vote2 증가. battleId: {}", battleId);
                }
            }
        } else {
            // 기존 투표가 없는 경우 새로운 투표 추가
            battleMapper.insertBattleVote(battleId, userId, postId);
            log.info("새로운 투표 추가됨.");

            if (postId.equals(battle.getPostId1())) {
                battleMapper.incrementVote1Cnt(battleId);
                log.info("vote1 증가. battleId: {}", battleId);
            } else if (postId.equals(battle.getPostId2())) {
                battleMapper.incrementVote2Cnt(battleId);
                log.info("vote2 증가. battleId: {}", battleId);
            }
        }
    }

    public Map<String, Boolean> getVoteState(Long battleId, Long userId) {
        Long votePostId = battleMapper.getBattleVote(battleId, userId);
        Battle battle = battleMapper.getBattleById(battleId);

        Map<String, Boolean> voteState = new HashMap<>();
        voteState.put("post1Vote", votePostId != null && votePostId.equals(battle.getPostId1()));
        voteState.put("post2Vote", votePostId != null && votePostId.equals(battle.getPostId2()));
        return voteState;
    }
}