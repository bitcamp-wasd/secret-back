package com.example.battle.service;

import com.example.battle.dto.BattleDto;
import com.example.battle.dto.CommentDto;
import com.example.battle.dto.info.PostDto;
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

        // 가상의 postDto 담기
        PostDto post1 = new PostDto();
        post1.setVideoId(101L);
        post1.setTitle("청팀 동영상제목");
        post1.setThumbnailPath("post1의 썸네일");
        post1.setUserNickname("김융");
        post1.setViews(500L);
        post1.setUploadDate(LocalDate.of(2023, 6, 1));
        post1.setLength(120);

        PostDto post2 = new PostDto();
        post2.setVideoId(102L);
        post2.setTitle("백팀 동영상제목");
        post2.setThumbnailPath("post2의 썸네일");
        post2.setUserNickname("김융");
        post2.setViews(700L);
        post2.setUploadDate(LocalDate.of(2023, 6, 2));
        post2.setLength(150);

        // BattleDto에 PostDto 설정
        battleDto.setPostId1(post1);
        battleDto.setPostId2(post2);

        return battleDto;
    }
}
