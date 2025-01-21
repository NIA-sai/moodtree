package com.moodtree.moodtree.service;

import com.moodtree.moodtree.model.po.ResponseResult;
import com.moodtree.moodtree.model.vo.IntegratedAnswer;
import com.moodtree.moodtree.model.vo.Result;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
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

    private String apiUrl = "https://chatglm.cn/main/gdetail/6789d124edd4a13c4571b2d6";
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
            return Result.success(responseResult);
        }catch(Exception e)
        {
            return Result.failure(600, "调用ChatGLM智能体失败");
        }
    }

    public Result imageFromResponseResult(ResponseResult responseResult)//获取生成的图片
    {
        try{
            if (responseResult != null && responseResult.getStatus().equals("finish")) {
                for (ResponseResult.Part part : responseResult.getOutput()) {
                    ResponseResult.Content content = part.getContent();
                    if ("image".equals(content.getType())) {
                        // 获取图片URL
                        List<ResponseResult.Image> images = content.getImage();
                        for (ResponseResult.Image image : images) {
                            String imageUrl = image.getImage_url();
                            // 处理图片URL
                            return Result.success(imageUrl);//这里图片存在List里，但只返回了一张图，要改改写法
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
            if (responseResult != null && responseResult.getStatus().equals("finish")) {
                for (ResponseResult.Part part : responseResult.getOutput()) {
                    ResponseResult.Content content = part.getContent();
                    if ("text".equals(content.getType())) {
                        String text = content.getText();
                        // 处理文本
                        return Result.success(text);
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
            return Result.failure(720, "获取智能回答失败");
        }
    }
}
