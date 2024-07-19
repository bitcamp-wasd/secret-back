package com.example.user.api;

import com.example.user.dto.battle.response.BattleMyCommentDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(name = "BattleRestApi", url = "${openfeign.url.battle}")
public interface BattleRestApi {

    @GetMapping("myComment")
    List<BattleMyCommentDto> getMyBattleComments(@RequestParam("userId") Long userId);

    @DeleteMapping("myComments")
    void deleteBattleComments(@RequestParam("userId") Long userId,
                              @RequestParam("battleCommentIds") List<Long> battleCommentIds);
}
