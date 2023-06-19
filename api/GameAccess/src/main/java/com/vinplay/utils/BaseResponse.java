/**
 * Archie
 */
package com.vinplay.utils;

import java.io.Serializable;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;

/**
 * @author Archie
 *
 */
public class BaseResponse<T> implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 2487529235400417067L;

	private int code;

	private String message;

	private T data;

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public T getData() {
		return data;
	}

	public void setData(T data) {
		this.data = data;
	}

	public BaseResponse(int code, String message, T data) {
		this.code = code;
		this.message = message;
		this.data = data;
	}
	
	
	public BaseResponse() {
	}

	public static String error(int errorCode, String message) {
		return "{\"code\":" + errorCode + ",\"message\":\"" + message + "\",\"data\":null}";
	}
	
	public static String error(String errorCode, String message) {
		return "{\"code\":" + errorCode + ",\"message\":\"" + message + "\",\"data\":null}";
	}

	public String success(T data) {
		this.code = 0;
		this.message = "SUCCESS";
		this.data = data;
		return this.toJson();
	}
	
	public BaseResponse(T data) {
		this.code = 0;
		this.message = "SUCCESS";
		this.data = data;
	}
	public BaseResponse(int code, String message) {
		//error
		this.code = code;
		this.message = message;
	}

	
	public String toJson() {
		ObjectWriter ow = new ObjectMapper().writer();ow.with(SerializationFeature.INDENT_OUTPUT);
		try {
			String json = ow.writeValueAsString(this);
			return json;
		} catch (Exception e) {
			return null;
		}
	}

}
