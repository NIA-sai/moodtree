package com.moodtree.moodtree.service;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.moodtree.moodtree.model.vo.Result;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Date;
import java.util.Map;

@Service
public class TokenService
{
	private String accessToken;
	private Long tokenExpiry;
	
	@Value ( "${chatglm.api-key}" )
	private String apiKey;
	
	@Value ( "${chatglm.api-secret}" )
	private String apiSecret;
	
	private final RestTemplate restTemplate = new RestTemplate ();
	
	public Result getAccessToken ()
	{
		try
		{
			if ( accessToken == null || ( boolean ) isTokenExpired ().getData () )
			{
				refreshToken ();
			}
			//System.out.println ( accessToken );
			return Result.success ( accessToken );
		}
		catch ( Exception e )
		{
			System.out.println ( e.toString () );
			return Result.failure ( 500 , "获取token失败" );
		}
	}
	
	private Result isTokenExpired ()
	{
		try
		{
			if ( new Date ().getTime () >= tokenExpiry )
			{
				return Result.success ( true );
			}
			return Result.success ( false );
		}
		catch ( Exception e )
		{
			return Result.failure ( 510 , "无法验证token是否过期" );
		}
	}
	
	//先随地拉一个类
	static class TokenResponse
	{
		String message;
		Integer status;
		TokenDetail result;
		
		public String getMessage ()
		{
			return message;
		}
		
		public Integer getStatus ()
		{
			return status;
		}
		
		public TokenDetail getResult ()
		{
			return result;
		}
		
		static class TokenDetail
		{
			@JsonProperty( "access_token" )
			String accessToken;
			@JsonProperty( "expires_in" )
			Integer expiresIn;
			@JsonProperty("token_expires")
			Long tokenExpires;
			
			public String getAccessToken ()
			{
				return accessToken;
			}
			
			public Integer getExpiresIn ()
			{
				return expiresIn;
			}
			
			public Long getTokenExpires ()
			{
				return tokenExpires;
			}
		}
	}
	
	public Result refreshToken ()
	{
		try
		{
			String url = "https://chatglm.cn/chatglm/assistant-api/v1/get_token";
			Map < String, String > request = Map.of (
					"api_key" , apiKey ,
					"api_secret" , apiSecret
			);
			TokenResponse response = restTemplate.postForObject ( url , request , TokenResponse.class );
			accessToken = ( String ) response.getResult ( ).getAccessToken ();
			//Long expiresIn = ( Long ) response.get ( "expires_in" );
			tokenExpiry = response.getResult ().getTokenExpires ();
			//System.out.println (response.getMessage ()+"<-message");
			//System.out.println (accessToken);
			return Result.success ( accessToken );
		}
		catch ( Exception e )
		{
			System.out.println ( e.toString () );
			return Result.failure ( 500 , "获取token失败" );
		}
	}
}
