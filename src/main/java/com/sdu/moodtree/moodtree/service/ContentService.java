package com.sdu.moodtree.moodtree.service;

import com.sdu.moodtree.moodtree.entity.Response;
import com.sdu.moodtree.moodtree.util.ThirdApi;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@Service
public class ContentService
{
	public Mono <Response> aiGenerate ( String prompt )
	{
		return ThirdApi.getMoodTherapy ( prompt );
	}
}
