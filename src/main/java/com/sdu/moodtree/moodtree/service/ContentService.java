package com.sdu.moodtree.moodtree.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sdu.moodtree.moodtree.entity.Response;
import com.sdu.moodtree.moodtree.util.ThirdApi;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
public class ContentService
{
	public Mono < Response > aiGenerate ( String prompt )
	{
		return ThirdApi.getMoodTherapy ( prompt );
	}
	
	public Mono < Response > aiAnalysis ( List < Object > datas )
	{try
		{
			StringBuilder prompt = new StringBuilder ();
			ObjectMapper objectMapper = new ObjectMapper ();
			for ( Object data : datas )
				prompt.append ( objectMapper.writeValueAsString ( data ) );
			return ThirdApi.getMoodAnalysis ( prompt.toString () );
		}catch ( JsonProcessingException e )
	{
		e.printStackTrace ();
		return Mono.just ( new Response ( 500, "额，看来偷懒不行啊", null ) );
	}
	}
}
