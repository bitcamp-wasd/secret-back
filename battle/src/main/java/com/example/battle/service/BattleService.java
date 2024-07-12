package com.example.battle.service;

import com.example.battle.api.UserRestApi;
import com.example.battle.api.VideoRestApi;
import com.example.battle.dto.BattleDto;
import com.example.battle.dto.post.response.PostInfoDto;
import com.example.battle.entity.Battle;
import com.example.battle.mapper.BattleMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
@Log4j2
public class BattleService {

    private final BattleMapper battleMapper;
    private final UserRestApi userRestApi;
    private final VideoRestApi videoRestApi;

    // 게시글 조회
    public BattleDto getBattleDetail(Long battleId) {

        // 조회수 증가 (중복 적용 안되게 하는 로직은 통합후 설정)
        battleMapper.incrementViews(battleId);

        Battle battle = battleMapper.getBattleById(battleId);
        BattleDto battleDto = new BattleDto();

        battleDto.setBattleId(battle.getBattleId());
        battleDto.setTitle(battle.getTitle());
        battleDto.setViews((long) battle.getViews());
        battleDto.setEndDate(battle.getEndDate());
        battleDto.setVote1Cnt((long) battle.getVote1Cnt());
        battleDto.setVote2Cnt((long) battle.getVote2Cnt());

        PostInfoDto post1 = videoRestApi.videoInfo(battle.getPostId1());
        PostInfoDto post2 = videoRestApi.videoInfo(battle.getPostId2());

        // BattleDto에 PostDto 설정
        battleDto.setPostId1(post1);
        battleDto.setPostId2(post2);

        return battleDto;
    }
}
