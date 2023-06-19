package com.vinplay.payment.entities;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;

public class Response {
	
	private String message;
	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		
		this.message = message;
	}
	private int code;
	public int getCode() {
		return code;
	}
	
	public void setCode(int code) {
		setMessage(code == 0 ? "success" : "failed");
		this.code = code;
	}
	
	private String data;
	public String getData() {
		return data;
	}
	
	public void setData(String data) {
		this.data = data;
	}
	
	public static Response error(int code, String message) {
		return new Response(message, code, null);
	}

	public Response(String message, int code, String data) {
		super();
		this.message = message;
		this.code = code;
		this.data = data;
	}

	public Response(String message, int code) {
		super();
		this.message = message;
		this.code = code;
		this.data = null;
	}

	public Response(int code, String data) {
		super();
		this.message = code == 1 ? "failed" : "success";
		this.code = code;
		this.data = data;
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
