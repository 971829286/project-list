package com.souche.bulbous.web.json;

import com.souche.optimus.core.web.Result;

public abstract class AbstractAction {
	
    // /**
    // * 从context中获取数组参数
    // * @param paramName
    // * @param context
    // * @return
    // */
    // public String[] getArrayParameter(String paramName,Context context){
    // if(StringUtils.isEmpty(paramName)){
    // return null;
    // }
    // return context.get(paramName + "-array", String[].class);
    // }
	
	public <T> Result<T> buildSuccessResult(T data){
		return new Result<T>().setSuccess(true).setCode("200").setData(data);
	}
	
	public <T> Result<T> buildSuccessResult(String message,T data){
		return new Result<T>().setSuccess(true).setCode("200").setMsg(message).setData(data);
	}
	
	public Result<Object> buildFailuerResult(String message){
		return new Result<Object>().setSuccess(false).setCode("500").setMsg(message);
	}
	
	public <T> Result<T> buildFailuerResult(String message,T data){
		return new Result<T>().setSuccess(false).setCode("500").setMsg(message).setData(data);
	}
	
	public <T> Result<T> success(String message,T data){
		return new Result<T>().setSuccess(true).setCode("200").setMsg(message).setData(data);
	}
	
	public <T> Result<T> error(String message,T data){
		return new Result<T>().setSuccess(false).setCode("500").setMsg(message).setData(data);
	}

}
