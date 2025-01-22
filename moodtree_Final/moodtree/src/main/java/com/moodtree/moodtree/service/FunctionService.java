package com.moodtree.moodtree.service;

import com.moodtree.moodtree.model.po.ResponseResult;
import com.moodtree.moodtree.model.vo.IntegratedAnswer;

import com.moodtree.moodtree.model.vo.Result;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service
public class FunctionService
{
    private final RestTemplate restTemplate;
    private final TokenService tokenService;
    public FunctionService(RestTemplate restTemplate, TokenService tokenService)
    {
        this.restTemplate = restTemplate;
        this.tokenService = tokenService;
    }

    private String apiUrl = "https://chatglm.cn/chatglm/assistant-api/v1";
    public Result callChatGLM(String mood)
    {
        try{
            String token = tokenService.getAccessToken().getData().toString();

            String assistantId = "6789d124edd4a13c4571b2d6";
            String prompt = mood;

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set("Authorization", "Bearer " + token);

            Map<String, Object> requestBody = new HashMap<>();
            requestBody.put("assistant_id", assistantId);
            requestBody.put("prompt", prompt);

            HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(requestBody, headers);

            RestTemplate restTemplate = new RestTemplate();

            ResponseResult responseResult = restTemplate.postForObject(apiUrl + "/stream_sync", requestEntity, ResponseResult.class);

            if (responseResult != null && responseResult.getStatus().equals("0")) {
                return Result.success(responseResult);
            }
            return Result.failure(610, "ChatGLM智能体创建内容失败");
        }catch(Exception e)
        {
            return Result.failure(600, "调用ChatGLM智能体失败");
        }

    }


    public Result imageFromResponseResult(ResponseResult responseResult)//获取生成的图片
    {
        try{
            for (ResponseResult.ResultInResponseResult.Part part : responseResult.getResult().getOutput()) {
                for(ResponseResult.ResultInResponseResult.Content content : part.getContent()){
                    if ("image".equals(content.getType())) {
                        // 获取图片URL
                        List<ResponseResult.ResultInResponseResult.Content.Image> images = content.getImage();
                        for (ResponseResult.ResultInResponseResult.Content.Image image : images) {
                            String imageUrl = image.getImage_url();
                            // 处理图片URL
                            return Result.success(imageUrl);
                            //图片仅有一张，直接返回即可。
                            //但是我写的接收图片的容器有容纳多张图片的潜力，可为新功能做准备。
                        }
                    }
                }
            }
        }catch(Exception e)
        {
            return Result.failure(700, "获取图片失败");
        }
        return Result.failure(700, "获取图片失败");
    }
    public Result textFromResponseResult(ResponseResult responseResult)//获取生成的文本
    {
        try{
            for (ResponseResult.ResultInResponseResult.Part part : responseResult.getResult().getOutput()) {
                for(ResponseResult.ResultInResponseResult.Content content : part.getContent()){
                    if ("text".equals(content.getType())) {
                        // 获取文本
                        return Result.success(content.getText());
                    }
                }
            }
        }catch(Exception e)
        {
            return Result.failure(710, "获取文本失败");
        }
        return Result.failure(710, "获取文本失败");
    }

    public Result integratedAnswer(String mood)
    {
        try{
            ResponseResult responseResult = (ResponseResult) callChatGLM(mood).getData();
            IntegratedAnswer integratedAnswer = new IntegratedAnswer(imageFromResponseResult(responseResult).getData().toString(), textFromResponseResult(responseResult).getData().toString());
            return Result.success(integratedAnswer);
        }catch(Exception e)
        {
            return Result.failure(800, "获取智能回答失败");
        }
    }
}
