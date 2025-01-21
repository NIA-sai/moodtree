package com.moodtree.moodtree.controller;

import com.moodtree.moodtree.model.vo.Result;
import com.moodtree.moodtree.service.FunctionService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
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

    /*@PostMapping("/generateResponse")
    public Result generateResponse(@RequestParam String mood) {
        return functionService.callChatGLM(mood);
    }*/

    @PostMapping("/integratedAnswer")
    public Result integratedAnswer(@RequestParam("mood") String mood) {
        System.out.println ("test!");
        return functionService.integratedAnswer(mood);
    }
}
