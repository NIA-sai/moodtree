package com.moodtree.moodtree.controller;

import com.moodtree.moodtree.model.vo.Result;
import com.moodtree.moodtree.service.FunctionService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class FunctionController
{
    FunctionService functionService;
    public FunctionController(FunctionService functionService)
    {
        this.functionService = functionService;
    }

    //只需调用这个接口即可
    @PostMapping("/integratedAnswer")//http://localhost:8080/integratedAnswer
    public Result integratedAnswer(@RequestParam("mood") String mood) {
        return functionService.integratedAnswer(mood);
    }
}
