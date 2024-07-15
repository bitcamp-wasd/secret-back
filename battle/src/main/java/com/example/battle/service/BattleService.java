package com.example.battle.service;

import com.example.battle.api.UserRestApi;
import com.example.battle.api.VideoRestApi;
import com.example.battle.dto.BattleDto;
import com.example.battle.dto.post.response.PostInfoDto;
import com.example.battle.entity.Battle;
import com.example.battle.mapper.BattleMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

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

        Long postId1 = battle.getPostId1();
        Long postId2 = battle.getPostId2();

        PostInfoDto post1 = videoRestApi.videoInfo(postId1);
        PostInfoDto post2 = videoRestApi.videoInfo(postId2);

        // BattleDto에 PostDto 설정
        battleDto.setPostId1(post1);
        battleDto.setPostId2(post2);

        return battleDto;
    }

    public Slice<BattleDto> getBattles(String state, Pageable pageable) {
        int offset = (int)pageable.getOffset();
        int limit = pageable.getPageSize();

        List<Battle> battles = battleMapper.findByStateBattle(state, limit, offset);
        log.info("battles: {}:",  battles);

        int total = battleMapper.countByState(state);

        List<BattleDto> battleDtos = battles.stream().map(battle -> {
            log.info("battle: {}", battle);

            BattleDto battleDto = new BattleDto();

            battleDto.setBattleId(battle.getBattleId());
            battleDto.setTitle(battle.getTitle());
            battleDto.setViews((long) battle.getViews());
            battleDto.setEndDate(battle.getEndDate());
            battleDto.setVote1Cnt((long) battle.getVote1Cnt());
            battleDto.setVote2Cnt((long) battle.getVote2Cnt());

            Long postId1 = battle.getPostId1();
            Long postId2 = battle.getPostId2();

            log.info("postId1: {}, postId2: {}", postId1, postId2);

            PostInfoDto post1 = videoRestApi.videoInfo(postId1);
            PostInfoDto post2 = videoRestApi.videoInfo(postId2);

            // BattleDto에 PostDto 설정
            battleDto.setPostId1(post1);
            battleDto.setPostId2(post2);

            return battleDto;
        }).collect(Collectors.toList());

        log.info("battleDtos: {}:",  battleDtos);

        boolean hasNext = offset + limit < total;

        return new SliceImpl<>(battleDtos, pageable, hasNext);
    }

    // 내가올린 배틀 게시판
    public Slice<BattleDto> getUserBattles(Long userId, Pageable pageable) {
        int offset = (int)pageable.getOffset();
        int limit = pageable.getPageSize();

        List<Battle> battles = battleMapper.findByMyBattle(userId, limit, offset);

        int total = battleMapper.countByMyBattle(userId);

        List<BattleDto> battleDtos = battles.stream().map(battle -> {

            BattleDto battleDto = new BattleDto();

            battleDto.setBattleId(battle.getBattleId());
            battleDto.setTitle(battle.getTitle());
            battleDto.setViews((long) battle.getViews());
            battleDto.setEndDate(battle.getEndDate());
            battleDto.setVote1Cnt((long) battle.getVote1Cnt());
            battleDto.setVote2Cnt((long) battle.getVote2Cnt());

            Long postId1 = battle.getPostId1();
            Long postId2 = battle.getPostId2();

            PostInfoDto post1 = videoRestApi.videoInfo(postId1);
            PostInfoDto post2 = videoRestApi.videoInfo(postId2);

            // BattleDto에 PostDto 설정
            battleDto.setPostId1(post1);
            battleDto.setPostId2(post2);

            return battleDto;
        }).collect(Collectors.toList());

        boolean hasNext = offset + limit < total;

        return new SliceImpl<>(battleDtos, pageable, hasNext);
    }

}
