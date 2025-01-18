package com.sdu.moodtree.moodtree.controller;

import com.sdu.moodtree.moodtree.entity.Response;
import com.sdu.moodtree.moodtree.service.ContentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping ( "/content" )
public class ContentController
{
	@Autowired
	private ContentService contentService;
	
	@GetMapping ( "/AIGC/{prompt}" )
	public Mono <Response> aiGenerate ( @PathVariable String prompt )
	{
		return contentService.aiGenerate ( prompt );
	}
	
	
	
	
	
	
	
	
	
	public void setContentService ( ContentService contentService )
	{
		this.contentService = contentService;
	}
}
