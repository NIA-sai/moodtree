package com.moodtree.moodtree.model.vo;

public class Result
{
    private int code;
    private String message;
    private Object data;

    public Result(int code, String message, Object data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public Object getData() {
        return data;
    }

    public String getMessage() {
        return message;
    }

    public int getCode() {
        return code;
    }

    public static Result success(Object data) {
        return new Result(100,"success",data);
    }

    public static Result failure(int code, String message) {
        return new Result(code,message,null);
    }
}
