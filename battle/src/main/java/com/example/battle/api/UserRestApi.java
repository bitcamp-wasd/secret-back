package com.example.battle.api;

import com.example.battle.dto.user.response.UserRankInfoDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "UserRestApi", url = "${openfeign.url.user}")
public interface UserRestApi {

    @GetMapping("rankInfo")
    public UserRankInfoDto userRankInfo(@RequestParam("userId") Long userId);
}
