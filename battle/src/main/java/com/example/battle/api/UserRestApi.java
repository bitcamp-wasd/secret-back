package com.example.battle.api;

import com.example.battle.dto.user.response.UserRankInfoDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "UserRestApi", url = "${openfeign.url.user}")
public interface UserRestApi {

    @GetMapping("rankInfo")
    public UserRankInfoDto userRankInfo(@RequestParam("userId") Long userId);

    @PutMapping("pointInfo")
    void  addUserPoints(@RequestParam("userId") Long userId,
                        @RequestParam("point") int point);
}
