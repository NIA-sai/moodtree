package com.sdu.moodtree.moodtree.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.sdu.moodtree.moodtree.entity.Response;
import com.sdu.moodtree.moodtree.service.ContentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
@RequestMapping ( "/content" )
public class ContentController
{
	@Autowired
	private ContentService contentService;
	
	@GetMapping ( "/AIGC/{prompt}" )
	public Mono < Response > aiGenerate ( @PathVariable String prompt )
	{
		return contentService.aiGenerate ( prompt );
	}
	
	@PostMapping ( "/AIGC" )
	public Mono < Response > aiAnalysis ( @RequestBody List < Object > data )
	{
		return contentService.aiAnalysis ( data );
	}
	
	
	public void setContentService ( ContentService contentService )
	{
		this.contentService = contentService;
	}
}
