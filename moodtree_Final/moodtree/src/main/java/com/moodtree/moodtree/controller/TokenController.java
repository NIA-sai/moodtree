package com.moodtree.moodtree.controller;

import com.moodtree.moodtree.model.vo.Result;
import com.moodtree.moodtree.service.TokenService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TokenController
{
    private final TokenService tokenService;
    public TokenController(TokenService tokenService) {
        this.tokenService = tokenService;
    }

    /*@GetMapping("/getToken")//获取token的值，仅开发阶段使用，前端无需调用
    public Result getToken() {
        return tokenService.getAccessToken();
    }*/

}
