package com.moodtree.moodtree.service;

import com.moodtree.moodtree.model.vo.Result;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Date;
import java.util.Map;

@Service
public class TokenService {
    private String accessToken;
    private Long tokenExpiry;

    @Value("${chatglm.api-key}")
    private String apiKey;

    @Value("${chatglm.api-secret}")
    private String apiSecret;

    private final RestTemplate restTemplate = new RestTemplate();

    public Result getAccessToken() {
        try{
            if (accessToken == null || (boolean)isTokenExpired().getData()) {
                accessToken=(String)refreshToken().getData();
            }
            return Result.success(accessToken);
        }catch(Exception e)
        {
            return Result.failure(500, "获取token失败");
        }
    }

    private Result isTokenExpired() {
        try{
            if(new Date().getTime() >= tokenExpiry)
            {
                return Result.success(true);
            }
            return Result.success(false);
        }
        catch (Exception e)
        {
            return Result.failure(510, "无法验证token是否过期");
        }
    }

    public Result refreshToken() {
        try{
            String url = "https://chatglm.cn/chatglm/assistant-api/v1/get_token";
            Map<String, String> request = Map.of(
                    "api_key", apiKey,
                    "api_secret", apiSecret
            );
            Map<String, Object> response = restTemplate.postForObject(url, request, Map.class);
            //从response中拆出来result对象
            Map<String, Object> result = (Map<String, Object>) response.get("result");
            // 从result对象中获取access_token和expires_in
            accessToken = (String) result.get("access_token");
            //想办法吧expires_in转成long类型
            Integer integerValueOfExpiresIn = (Integer) result.get("expires_in");
            Long expiresIn =Long.valueOf(integerValueOfExpiresIn.longValue());
            tokenExpiry = new Date().getTime() + expiresIn * 1000;
            return Result.success(accessToken);
        }catch (Exception e)
        {
            return Result.failure(500, "获取token失败");
        }
    }
}
