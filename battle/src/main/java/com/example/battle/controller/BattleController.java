package com.example.battle.controller;

import com.example.battle.dto.BattleDto;
import com.example.battle.dto.CommentDto;
import com.example.battle.service.BattleCommentService;
import com.example.battle.service.BattleService;
import com.example.battle.service.BattleVoteService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Log4j2
public class BattleController {

    private final BattleService battleService;
    private final BattleVoteService battleVoteService;
    private final BattleCommentService battleCommentService;

    // 배틀 게시글 조회
    @GetMapping("/{battleId}")
    public ResponseEntity<BattleDto> getBattleDetail(@PathVariable Long battleId) {
        BattleDto battleDto = battleService.getBattleDetail(battleId);
        return ResponseEntity.ok(battleDto);

    }

    // 배틀 투표
    @PostMapping("/{battleId}/vote")
    public ResponseEntity<String> vote(@PathVariable Long battleId,
                                       @RequestParam Long userId,
                                       @RequestParam Long postId) {
        battleVoteService.vote(battleId, userId, postId);
        return ResponseEntity.ok("Vote successful");
    }

    // 댓글
    @GetMapping("/{battleId}/commentList")
    public ResponseEntity<Page<CommentDto>> getComments(@PathVariable Long battleId,
                                                        Pageable pageable) {
        Page<CommentDto> comments = battleCommentService.getComments(battleId, pageable);
        return ResponseEntity.ok(comments);
    }

    @PostMapping("/{battleId}/comment")
    public ResponseEntity<String> addComment(@PathVariable Long battleId,
                                             @RequestParam Long userId,
                                             @RequestBody CommentDto commentDto) {
        battleCommentService.addComment(battleId, userId, commentDto.getComment());
        return ResponseEntity.ok("댓글이 성공적으로 추가되었습니다.");
    }


    @PutMapping("/{battleId}/update/{battleCommentId}")
    public ResponseEntity<String> updateComment(@PathVariable Long battleId,
                                                @PathVariable Long battleCommentId,
                                                @RequestParam Long userId,
                                                @RequestBody CommentDto commentDto){
        battleCommentService.updateComment(battleId, battleCommentId, userId, commentDto.getComment());
        return ResponseEntity.ok("Comment Update successful");
    }

    @DeleteMapping("/{battleId}/delete/{battleCommentId}")
    public ResponseEntity<String> deleteComment(@PathVariable Long battleId,
                                                @PathVariable Long battleCommentId,
                                                @RequestParam Long userId){
        battleCommentService.deleteComment(battleId,battleCommentId, userId);
        return ResponseEntity.ok("Comment Delete successful");
    }

    @GetMapping("/{battleId}/count")
    public ResponseEntity<Integer> getCommentCountByBattleId(@PathVariable Long battleId) {
        return ResponseEntity.ok(battleCommentService.getCommentCountByBattleId(battleId));
    }
}