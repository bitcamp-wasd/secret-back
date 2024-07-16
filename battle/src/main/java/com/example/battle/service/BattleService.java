package com.example.battle.service;

import com.example.battle.api.UserRestApi;
import com.example.battle.api.VideoRestApi;
import com.example.battle.dto.BattleDto;
import com.example.battle.dto.BattleRegisterDto;
import com.example.battle.dto.post.response.PostIdDto;
import com.example.battle.dto.post.response.PostInfoDto;
import com.example.battle.entity.Battle;
import com.example.battle.mapper.BattleMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.URISyntaxException;
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

    private Long extractPostId(String url){
        try {
            URI uri = new URI(url);
            String path = uri.getPath();
            String[] segments = path.split("/");
            String lastSegment = segments[segments.length - 1];
            return Long.valueOf(lastSegment);

        } catch (URISyntaxException | NumberFormatException e) {
            e.printStackTrace();
            return null;
        }
    }


    // 배틀 등록
    public void registerBattle(Long userId, BattleRegisterDto battleRegisterDto) {

        Long videoId1 = extractPostId(battleRegisterDto.getUrl1());
        Long videoId2 = extractPostId(battleRegisterDto.getUrl2());

        PostIdDto postIdDto1 = videoRestApi.postIdInfo(videoId1);
        PostIdDto postIdDto2 = videoRestApi.postIdInfo(videoId2);

        Battle battle = new Battle();
        battle.setPostId1(postIdDto1.getPostId());
        battle.setPostId2(postIdDto2.getPostId());
        battle.setTitle(battleRegisterDto.getTitle());
        battle.setUserId(userId);
        battle.setCreateDate(LocalDate.now());
        battle.setEndDate(LocalDate.now().plusWeeks(1));
        battle.setState("진행중");
        battle.setVote1Cnt(0);
        battle.setVote2Cnt(0);
        battle.setViews(0);

        battleMapper.insertBattle(userId,battle);
    }


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
        int total = battleMapper.countByState(state);

        List<Battle> battles = battleMapper.findByStateBattle(state, limit, offset);
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
