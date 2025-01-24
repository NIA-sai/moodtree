package com.sdu.moodtree.moodtree.util;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import com.sdu.moodtree.moodtree.entity.Response;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.concurrent.TimeUnit;


@Component
@EnableScheduling
public class ThirdApi
{
	private static KeyAndSecret moodTherapyKeyAndSecret;
	
	private static String moodTherapyApiId;//?lang=zh  ??
	private static String moodTherapyToken;
	private static Long moodTherapyTokenExpireTime = 0L; //
	private static String moodAnalysisApiId;
	private static WebClient chatglmWebClient;
	
	@Value ( "${third_api.chatglm.url}" )
	private String chatglmURLInit;
	@Value ( "${third_api.chatglm.key}" )
	private String chatglmKeyInit;
	@Value ( "${third_api.chatglm.secret}" )
	private String chatglmSecretInit;
	@Value ( "${third_api.chatglm.mood_therapy_api_id}" )
	private String moodTherapyApiIdInit;
	@Value ( "${third_api.chatglm.mood_analysis_api_id}" )
	private String moodAnalysisApiIdInit;
	
	@PostConstruct
	public void init ()
	{
		chatglmWebClient = WebClient.create ( chatglmURLInit );
		moodTherapyKeyAndSecret = new KeyAndSecret ( chatglmKeyInit , chatglmSecretInit );
		moodTherapyApiId = moodTherapyApiIdInit;
		moodAnalysisApiId = moodAnalysisApiIdInit;
	}
	
	
	@Async
	@Scheduled ( fixedRate = 24*1000*60*60 )
	public static void refreshMoodTherapyToken ()
	{
		if ( moodTherapyTokenExpireTime-System.currentTimeMillis ()/1000 > TimeUnit.MINUTES.toSeconds ( 1 ) ) return;
		System.out.println ( "start refresh mood therapy token" );
		chatglmWebClient.post ().uri ( "/get_token" )
				.bodyValue ( moodTherapyKeyAndSecret ).retrieve ().bodyToMono ( ChatglmResponse.class )
				.subscribe ( response ->
				{
					if ( response == null ) throw new RuntimeException ( "get null when refresh mood therapy token" );
					if ( response.getStatus () == 1001 ) throw new RuntimeException ( "api_key is banned !" );
					if ( response.getStatus () == 1002 ) throw new RuntimeException ( "api_key or api_secret is invalid !" );
					moodTherapyToken = ( ( ChatglmTokenResult ) response.getResult () ).getToken ();
					moodTherapyTokenExpireTime = ( ( ChatglmTokenResult ) response.getResult () ).getTokenExpires ();
					System.out.println ( "refresh mood therapy token success " );
				} );
	}
	
	
	public static Mono < Response > getMoodTherapy ( String prompt )
	{
		
		ChatglmRequest request = new ChatglmRequest ( moodTherapyApiId , prompt );
		return getAndFilterResult ( request , new MoodTherapyResult () );
		
	}
	
	public static Mono < Response > getMoodAnalysis ( String prompt )
	{
		ChatglmRequest request = new ChatglmRequest ( moodAnalysisApiId , prompt );
		return getAndFilterResult ( request , new MoodTherapyResult ( "" , "没有捏" ) );
	}
	
	
	private static Mono < Response > getAndFilterResult ( ChatglmRequest request , MoodTherapyResult moodTherapyResult )
	{
		try
		{
			return
					chatglmWebClient.post ().uri ( "/stream_sync" )
							.header ( HttpHeaders.AUTHORIZATION , "Bearer "+moodTherapyToken )
							.bodyValue ( request ).retrieve ().bodyToMono ( JsonNode.class )
							.flatMap (
									rawResponse ->
									{
										if ( rawResponse == null ) throw new RuntimeException ( "null !" );
										
										
										rawResponse = rawResponse.get ( "result" ).get ( "output" );
										if ( rawResponse.isArray () )
										{
											for ( JsonNode node : rawResponse )
											{
												if ( node.get ( "model" ).asText ().equals ( "chatglm-all-tools" ) )
												{
													node = node.get ( "content" );
													if ( node.isArray () )
													{
														for ( JsonNode nodes : node )
														{
															if ( nodes.get ( "type" ).asText ().equals ( "text" ) )
																moodTherapyResult.setText ( nodes.get ( "text" ).asText () );
														}
													}
												}
												else if ( node.get ( "model" ).asText ().equals ( "cogview" ) )
												{
													node = node.get ( "content" );
													if ( node.isArray () )
													{
														for ( JsonNode nodes : node )
														{
															if ( nodes.get ( "type" ).asText ().equals ( "image" ) )
															{
																nodes = nodes.get ( "image" );
																if ( nodes.isArray () )
																{
																	for ( JsonNode nodess : nodes )
																	{
																		moodTherapyResult.setImgUrl ( nodess.get ( "image_url" ).asText () );
																	}
																}
															}
														}
													}
												}
											}
										}
										return Mono.just ( new Response ( 200 , "ok" , moodTherapyResult ) );
									}
							);
		}
		catch ( Exception e )
		{
			e.printStackTrace ();
			refreshMoodTherapyToken ();
			return Mono.just ( new Response ( 555 , "refreshed token, try again!" ) );
		}
	}
}


class KeyAndSecret
{
	@JsonProperty ( "api_key" )
	String key;
	@JsonProperty ( "api_secret" )
	String secret;
	
	KeyAndSecret ( String key , String secret )
	{
		this.key = key;
		this.secret = secret;
	}
	
	public String getSecret ()
	{
		return secret;
	}
	
	public String getKey ()
	{
		return key;
	}
}


//follows may should be another place
class ChatglmResponse
{
	String message;
	Object result;
	Integer status;
	
	public String getMessage ()
	{
		return message;
	}
	
	public void setMessage ( String message )
	{
		this.message = message;
	}
	
	public Object getResult ()
	{
		return result;
	}
	
	public void setResult ( ChatglmTokenResult result )
	{
		this.result = result;
	}
	
	public Integer getStatus ()
	{
		return status;
	}
	
	public void setStatus ( Integer status )
	{
		this.status = status;
	}
}


class ChatglmTokenResult
{
	@JsonProperty ( "access_token" )
	String token;
	@JsonProperty ( "token_expires" )
	Long tokenExpires;
	
	public String getToken ()
	{
		return token;
	}
	
	public void setToken ( String token )
	{
		this.token = token;
	}
	
	public Long getTokenExpires ()
	{
		return tokenExpires;
	}
	
	public void setTokenExpires ( Long tokenExpires )
	{
		this.tokenExpires = tokenExpires;
	}
}

class ChatglmRequest
{
	@JsonProperty ( "assistant_id" )
	private String assistantId;
	
	//conversation_id optional : unnecessary
	
	@JsonProperty ( "prompt" )
	private String prompt;
	
	//what is  meta_data?
	
	public ChatglmRequest ( String assistantId , String prompt )
	{
		this.assistantId = assistantId;
		this.prompt = prompt;
	}
	
	public String getAssistantId ()
	{
		return assistantId;
	}
	
	public String getPrompt ()
	{
		return prompt;
	}
}


class MoodTherapyResult
{
	String text;
	String imgUrl;
	
	public MoodTherapyResult ( String text , String imgUrl )
	{
		this.text = text;
		this.imgUrl = imgUrl;
	}
	
	public MoodTherapyResult ()
	{
	
	}
	
	public String getText ()
	{
		return text;
	}
	
	public String getImgUrl ()
	{
		return imgUrl;
	}
	
	public void setText ( String text )
	{
		this.text = text;
	}
	
	public void setImgUrl ( String imgUrl )
	{
		this.imgUrl = imgUrl;
	}
}


