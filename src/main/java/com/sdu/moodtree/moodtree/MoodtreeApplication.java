package com.sdu.moodtree.moodtree;

import com.sdu.moodtree.moodtree.util.ThirdApi;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling  // 启用定时任务功能
public class MoodtreeApplication
{
	
	public static void main ( String[] args )
	{
		SpringApplication.run ( MoodtreeApplication.class , args );
		System.out.println ( "success awa " );
	}
	
}
