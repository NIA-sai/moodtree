package com.sdu.moodtree.moodtree.util;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import com.sdu.moodtree.moodtree.entity.Response;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@Component
public class ThirdApi
{
	private static final String chatglmURL = "https://chatglm.cn/chatglm/assistant-api/v1";
	private static final WebClient chatglmWebClient = WebClient.create ( chatglmURL );
	
	
	private static final KeyAndSecret moodTherapyKeyAndSecret = new KeyAndSecret ( "7236fd914267180e" , "6dfcf6ba8e3ebb2524fe7168b4a9ad96" );
	private static final String moodTherapyApiId = "6789d124edd4a13c4571b2d6";//?lang=zh  ??
	private static String moodTherapyToken;
	
	
	private static void refreshMoodTherapyToken ()
	{
		ChatglmResponse response = chatglmWebClient.post ().uri ( "/get_token" )
				.bodyValue ( moodTherapyKeyAndSecret ).retrieve ().bodyToMono ( ChatglmResponse.class )
				.subscribeOn ( Schedulers.boundedElastic () )
				.block ();
		if ( response == null ) throw new RuntimeException ( "null !" );
		if ( response.getStatus () == 1001 ) throw new RuntimeException ( "api_key is banned !" );
		if ( response.getStatus () == 1002 ) throw new RuntimeException ( "api_key or api_secret is invalid !" );
		moodTherapyToken = ( ( ChatglmTokenResult ) response.getResult () ).getToken ();
	}

	public static Mono < Response > getMoodTherapy ( String prompt )
	{
		if ( moodTherapyToken == null )//or expired
		{
			Mono.fromCallable ( () ->
					{
						refreshMoodTherapyToken ();
						return null;
					} )
					.subscribeOn ( Schedulers.boundedElastic () )
					.block ();
		}
		ChatglmRequest request = new ChatglmRequest ( moodTherapyApiId , prompt );
		MoodTherapyResult moodTherapyResult = new MoodTherapyResult ();
		System.out.println ( request.getPrompt () );
		System.out.println ( moodTherapyToken );
		return chatglmWebClient.post ().uri ( "/stream_sync" )
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
	@JsonProperty ( "expires_in" )
	Integer expiresIn;
	
	public String getToken ()
	{
		return token;
	}
	
	public void setToken ( String token )
	{
		this.token = token;
	}
	
	public Integer getExpiresIn ()
	{
		return expiresIn;
	}
	
	public void setExpiresIn ( Integer expiresIn )
	{
		this.expiresIn = expiresIn;
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


