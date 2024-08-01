package com.example.battle.api;

import com.example.battle.dto.user.response.UserApiInfoDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "UserRestApi", url = "${openfeign.url.user}")
public interface UserRestApi {

    @GetMapping("info")
    public UserApiInfoDto userApiInfo(@RequestParam("userId") Long userId);

    @PutMapping("pointInfo")
    void  addUserPoints(@RequestParam("userId") Long userId,
                        @RequestParam("point") int point);
}
