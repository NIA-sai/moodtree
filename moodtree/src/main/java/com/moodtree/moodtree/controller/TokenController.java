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

    /*@GetMapping("/getToken")
    public Result getToken() {
        return tokenService.getAccessToken();
    }*/

}
