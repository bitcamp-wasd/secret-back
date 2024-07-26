package com.example.battle.service;

import com.example.battle.api.UserRestApi;
import com.example.battle.api.VideoRestApi;
import com.example.battle.dto.BattleDto;
import com.example.battle.dto.BattleRegisterDto;
import com.example.battle.dto.post.response.PostIdDto;
import com.example.battle.dto.post.response.PostInfoDto;
import com.example.battle.dto.post.response.PostUserIdDto;
import com.example.battle.entity.Battle;
import com.example.battle.entity.BattleVoteList;
import com.example.battle.mapper.BattleMapper;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
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

    @Scheduled(fixedRate = 60000) // 임시 1분마다 실행
    @Transactional
    public void checkEndBattles(){
        LocalDateTime currentDate = LocalDateTime.now();
        List<Battle> endedBattles = battleMapper.findEndBattles(currentDate);
        for (Battle battle : endedBattles) {
            endBattle(battle.getBattleId());
        }
    }

    @Transactional
    public void endBattle(Long battleId){
        Battle battle = battleMapper.getBattleById(battleId);
        if(battle == null){
            throw new IllegalArgumentException("Battle not found");
        }

        Long postId1 = battle.getPostId1();
        Long postId2 = battle.getPostId2();

        int vote1Cnt = battle.getVote1Cnt();
        int vote2Cnt = battle.getVote2Cnt();

        if (vote1Cnt == vote2Cnt){

            PostUserIdDto postUserId1 = videoRestApi.postUserIdInfo(postId1);
            Long userId1 = postUserId1.getUserId();
            userRestApi.addUserPoints(userId1,100);

            PostUserIdDto postUserId2 = videoRestApi.postUserIdInfo(postId2);
            Long userId2 = postUserId2.getUserId();
            userRestApi.addUserPoints(userId2,100);
        } else {

            Long winPostId = vote1Cnt > vote2Cnt ? postId1 : postId2;
            PostUserIdDto postUserId = videoRestApi.postUserIdInfo(winPostId);
            Long winUserId = postUserId.getUserId();
            userRestApi.addUserPoints(winUserId, 200);
        }

        battleMapper.updateBattleState(battleId, "종료");

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
        battle.setCreateDate(LocalDateTime.now());
        battle.setEndDate(LocalDateTime.now().plusWeeks(1));
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
        if (battle == null){
            throw new IllegalArgumentException("해당 게시글을 찾을 수 없습니다.");
        }
        BattleDto battleDto = new BattleDto();

        battleDto.setBattleId(battle.getBattleId());
        battleDto.setTitle(battle.getTitle());
        battleDto.setViews((long) battle.getViews());
        battleDto.setUserId(battle.getUserId());
        battleDto.setEndDate(battle.getEndDate());
        battleDto.setVote1Cnt((long) battle.getVote1Cnt());
        battleDto.setVote2Cnt((long) battle.getVote2Cnt());

        try{

            Long postId1 = battle.getPostId1();
            Long postId2 = battle.getPostId2();

            PostInfoDto post1 = videoRestApi.videoInfo(postId1);
            PostInfoDto post2 = videoRestApi.videoInfo(postId2);

            // BattleDto에 PostDto 설정
            battleDto.setPostId1(post1);
            battleDto.setPostId2(post2);

        }catch (FeignException.NotFound e){
            // 비디오가 삭제된 경우 예외처리
            deleteBattleRelatedData(battleId);
            throw new IllegalStateException("해당게시물이 삭제 되었습니다");

        }

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
            battleDto.setUserId(battle.getUserId());
            battleDto.setEndDate(battle.getEndDate());
            battleDto.setVote1Cnt((long) battle.getVote1Cnt());
            battleDto.setVote2Cnt((long) battle.getVote2Cnt());

            boolean shouldDelete = false;

            try {
                Long postId1 = battle.getPostId1();
                PostInfoDto post1 = videoRestApi.videoInfo(postId1);
                battleDto.setPostId1(post1);
            } catch (FeignException e) {
                // postId1를 찾지 못한 경우 null로 설정
                battle.setPostId1(null);
                battleDto.setPostId1(null);
                // 데이터베이스에서 해당 postId를 null로 업데이트
                battleMapper.updatePostId1ToNull(battle.getBattleId());
                shouldDelete = true;
            }

            try {
                Long postId2 = battle.getPostId2();
                PostInfoDto post2 = videoRestApi.videoInfo(postId2);
                battleDto.setPostId2(post2);
            } catch (FeignException e) {
                // postId2를 찾지 못한 경우 null로 설정
                battle.setPostId2(null);
                battleDto.setPostId2(null);
                // 데이터베이스에서 해당 postId를 null로 업데이트
                battleMapper.updatePostId2ToNull(battle.getBattleId());
                shouldDelete = true;
            }

            if (shouldDelete) {
                // 하나라도 postId가 null인 경우 battle 삭제
                deleteBattleRelatedData(battle.getBattleId());
                return null; // 리스트에서 제거
            }

            return battleDto;
        }).filter(Objects::nonNull).collect(Collectors.toList());

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
            battleDto.setUserId(battle.getUserId());
            battleDto.setEndDate(battle.getEndDate());
            battleDto.setVote1Cnt((long) battle.getVote1Cnt());
            battleDto.setVote2Cnt((long) battle.getVote2Cnt());

            boolean shouldDelete = false;

            try {
                Long postId1 = battle.getPostId1();
                PostInfoDto post1 = videoRestApi.videoInfo(postId1);
                battleDto.setPostId1(post1);
            } catch (FeignException e) {
                // postId1를 찾지 못한 경우 null로 설정
                battle.setPostId1(null);
                battleDto.setPostId1(null);
                // 데이터베이스에서 해당 postId를 null로 업데이트
                battleMapper.updatePostId1ToNull(battle.getBattleId());
                shouldDelete = true;
            }

            try {
                Long postId2 = battle.getPostId2();
                PostInfoDto post2 = videoRestApi.videoInfo(postId2);
                battleDto.setPostId2(post2);
            } catch (FeignException e) {
                // postId2를 찾지 못한 경우 null로 설정
                battle.setPostId2(null);
                battleDto.setPostId2(null);
                // 데이터베이스에서 해당 postId를 null로 업데이트
                battleMapper.updatePostId2ToNull(battle.getBattleId());
                shouldDelete = true;
            }

            if (shouldDelete) {
                // 하나라도 postId가 null인 경우 battle 삭제
                deleteBattleRelatedData(battle.getBattleId());
                return null; // 리스트에서 제거
            }
            return battleDto;
        }).filter(Objects::nonNull).collect(Collectors.toList());

        boolean hasNext = offset + limit < total;

        return new SliceImpl<>(battleDtos, pageable, hasNext);
    }

    public void deleteBattle(Long battleId, Long userId) {
        Battle battle = battleMapper.getBattleById(battleId);
        if (battle == null) {
            throw new IllegalArgumentException("Battle not found");
        }

        if (!battle.getUserId().equals(userId)) {
            throw new IllegalArgumentException("Battle user does not match");
        }

        deleteBattleRelatedData(battleId);
    }

    private void deleteBattleRelatedData(Long battleId) {
        try {
            battleMapper.deleteCommentsByBattleId(battleId);
            battleMapper.deleteVotesByBattleId(battleId);
            battleMapper.deleteBattleByBattleId(battleId);
        } catch (Exception e){
            throw new IllegalStateException("배틀 게시물 삭제 중 오류" + e.getMessage(),e);
        }
    }

}
