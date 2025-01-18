package com.sdu.moodtree.moodtree.entity;

public class Response
{
	private Integer code;
	private String msg;
	private Object data;
	
	public Response ( Integer code , String msg , Object data )
	{
		this.code = code;
		this.msg = msg;
		this.data = data;
	}
	
	public Response ( Integer code , String msg )
	{
		this.code = code;
		this.msg = msg;
	}
	
	
	public Integer getCode ()
	{
		return code;
	}
	
	public String getMsg ()
	{
		return msg;
	}
	
	public Object getData ()
	{
		return data;
	}
}
