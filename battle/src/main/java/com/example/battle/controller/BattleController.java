package com.example.battle.controller;

import com.example.battle.annotation.HeaderUserAuth;
import com.example.battle.dto.BattleDto;
import com.example.battle.dto.BattleRegisterDto;
import com.example.battle.dto.CommentDto;
import com.example.battle.dto.auth.UserAuth;
import com.example.battle.dto.battle.request.BattleCommentListDto;
import com.example.battle.dto.response.BattleMyCommentDto;
import com.example.battle.service.BattleCommentService;
import com.example.battle.service.BattleService;
import com.example.battle.service.BattleVoteService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@Log4j2
public class BattleController {

    private final BattleService battleService;
    private final BattleVoteService battleVoteService;
    private final BattleCommentService battleCommentService;

    // 배틀 등록
    @PostMapping("/auth/register")
    public ResponseEntity<String> registerBattle(@HeaderUserAuth UserAuth userAuth,
                                                 @RequestBody BattleRegisterDto battleRegisterDto) {
        battleService.registerBattle(userAuth.getUserId(), battleRegisterDto);
        return ResponseEntity.ok("success");
    }

    // 배틀 게시글 조회
    @GetMapping("/{battleId}")
    public ResponseEntity<BattleDto> getBattleDetail(@PathVariable Long battleId) {
        BattleDto battleDto = battleService.getBattleDetail(battleId);
        return ResponseEntity.ok(battleDto);

    }

    // 배틀 리스트
    @GetMapping("/list")
    public ResponseEntity<Slice<BattleDto>> getBattleList(@RequestParam(defaultValue = "0") int pageNumber,
                                                          @RequestParam(defaultValue = "10") int pageSize) {

        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        Slice<BattleDto> battles = battleService.getBattles("진행중", pageable);
        return ResponseEntity.ok(battles);

    }

    // 배틀 게시판 삭제
    @DeleteMapping("/auth/{battleId}/delete")
    public ResponseEntity<String> deleteBattle(@PathVariable Long battleId,
                                               @HeaderUserAuth UserAuth userAuth) {
        battleService.deleteBattle(battleId, userAuth.getUserId());
        return ResponseEntity.ok("success");
    }

    // 내가 올린 배틀 리스트
    @GetMapping("/auth/mybattle")
    public ResponseEntity<Slice<BattleDto>> getMyBattleList(@HeaderUserAuth UserAuth userAuth,
                                                            @RequestParam(defaultValue = "0") int pageNumber,
                                                            @RequestParam(defaultValue = "10") int pageSize) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        Slice<BattleDto> battles = battleService.getUserBattles(userAuth.getUserId(), pageable);
        return ResponseEntity.ok(battles);
    }

    // 배틀 투표
    @PostMapping("/auth/{battleId}/vote")
    public ResponseEntity<String> vote(@PathVariable Long battleId,
                                       @HeaderUserAuth UserAuth user,
                                       @RequestParam Long postId) {
        log.info("Received request for voting. BattleId: {}, User: {}, PostId: {}", battleId, user, postId);
        battleVoteService.vote(battleId, user.getUserId(), postId);
        return ResponseEntity.ok("Vote successful");
    }

    @GetMapping("/auth/{battleId}/state")
    public ResponseEntity<Map<String,Boolean>> getVoteState(@HeaderUserAuth UserAuth userAuth,
                                                            @PathVariable Long battleId) {
        Map<String, Boolean> voteState = battleVoteService.getVoteState(battleId, userAuth.getUserId());
        return ResponseEntity.ok(voteState);
    }

    // 댓글
    @GetMapping("/{battleId}/commentList")
    public ResponseEntity<Slice<CommentDto>> getComments(@PathVariable Long battleId,
                                                        @RequestParam(defaultValue = "0") int pageNumber,
                                                        @RequestParam(defaultValue = "5") int pageSize) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        Slice<CommentDto> comments = battleCommentService.getComments(battleId, pageable);
        return ResponseEntity.ok(comments);
    }

    @PostMapping("/auth/{battleId}/comment")
    public ResponseEntity<String> addComment(@PathVariable Long battleId,
                                             @HeaderUserAuth UserAuth user,
                                             @RequestBody CommentDto commentDto) {
        battleCommentService.addComment(battleId, user.getUserId(), commentDto.getComment());
        return ResponseEntity.ok("댓글이 성공적으로 추가되었습니다.");
    }


    @PutMapping("/auth/{battleId}/update/{battleCommentId}")
    public ResponseEntity<String> updateComment(@PathVariable Long battleId,
                                                @PathVariable Long battleCommentId,
                                                @HeaderUserAuth UserAuth user,
                                                @RequestBody CommentDto commentDto){
        battleCommentService.updateComment(battleId, battleCommentId, user.getUserId(), commentDto.getComment());
        return ResponseEntity.ok("Comment Update successful");
    }

    @DeleteMapping("/auth/{battleId}/delete/{battleCommentId}")
    public ResponseEntity<String> deleteComment(@PathVariable Long battleId,
                                                @PathVariable Long battleCommentId,
                                                @HeaderUserAuth UserAuth user){
        battleCommentService.deleteComment(battleId,battleCommentId, user.getUserId());
        return ResponseEntity.ok("Comment Delete successful");
    }

    @GetMapping("/{battleId}/count")
    public ResponseEntity<Integer> getCommentCountByBattleId(@PathVariable Long battleId) {
        return ResponseEntity.ok(battleCommentService.getCommentCountByBattleId(battleId));
    }

    // 마이페이지 댓글리스트
    @GetMapping("/auth/mycomment")
    public ResponseEntity<Page<BattleMyCommentDto>> getMyComments(@HeaderUserAuth UserAuth userAuth,
                                                                  @RequestParam(value = "page", defaultValue = "0") int page,
                                                                  @RequestParam(value = "size", defaultValue = "10") int size) {
        Page<BattleMyCommentDto> commentDtos = battleCommentService.getCommentsByUserId(userAuth.getUserId(), page, size);
        return ResponseEntity.ok(commentDtos);
    }

    @DeleteMapping("/auth/mycomment")
    public ResponseEntity<String> deleteMyComments(@HeaderUserAuth UserAuth userAuth,
                                                   @RequestBody BattleCommentListDto commentListDto) {
        battleCommentService.deleteComments(userAuth.getUserId(), commentListDto.getBattleCommentId());
        return ResponseEntity.ok("댓글이 성공적으로 삭제되었습니다");
    }
}