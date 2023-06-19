/**
 * Archie
 */
package com.vinplay.dto;

/**
 * @author Archie
 *
 */

import java.io.Serializable;

import com.vinplay.utils.Constants;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;

public class BaseResponseDto<T> implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1304264626523483927L;

	private int code;

	private String message;

	private T data;

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public T getData() {
		return data;
	}

	public void setData(T data) {
		this.data = data;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public BaseResponseDto(int code, String message, T data) {
		this.code = code;
		this.message = message;
		this.data = data;
	}

	public BaseResponseDto(int code, String message) {
		this.code = code;
		this.message = message;
	}

	public BaseResponseDto(String message) {
		this.code = Constants.FAIL;
		this.message = message;
	}

	public BaseResponseDto() {
	}
	public String toString() {
		ObjectWriter ow = new ObjectMapper().writer();
		ow.with(SerializationFeature.INDENT_OUTPUT);
		try {
			String json = ow.writeValueAsString(this);
			return json;
		} catch (Exception e) {
			return null;
		}
	}
	public String toJson() {
		ObjectWriter ow = new ObjectMapper().writer();
		ow.with(SerializationFeature.INDENT_OUTPUT);
		try {
			String json = ow.writeValueAsString(this);
			return json;
		} catch (Exception e) {
			return null;
		}
	}
}
