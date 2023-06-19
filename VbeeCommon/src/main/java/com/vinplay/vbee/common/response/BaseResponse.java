package com.vinplay.vbee.common.response;

import java.io.Serializable;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.vinplay.vbee.common.statics.Consts;

public class BaseResponse<T> implements Serializable {

	protected boolean success;
	protected String errorCode;
	protected String message;
	protected T statistic;
	protected long totalRecords;
	protected T data;


	public long getTotalRecords() {
		return totalRecords;
	}

	public void setTotalRecords(long totalRecords) {
		this.totalRecords = totalRecords;
	}

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public String getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
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

	public T getStatistic() {
		return statistic;
	}

	public void setStatistic(T statistic) {
		this.statistic = statistic;
	}

	public BaseResponse(boolean success, String errorCode, String message, T data, long totalRecords) {
		this.success = success;
		this.errorCode = errorCode;
		this.message = message;
		this.totalRecords = totalRecords;
		this.data = data;
	}

	public BaseResponse(boolean success, String errorCode, String message, T data, T statistic, long totalRecords) {
		this.success = success;
		this.errorCode = errorCode;
		this.message = message;
		this.totalRecords = totalRecords;
		this.statistic = statistic;
		this.data = data;
	}

	public BaseResponse(boolean success, String errorCode, String message, T data) {
		super();
		this.success = success;
		this.errorCode = errorCode;
		this.message = message;
		this.totalRecords = 0;
		this.statistic = null;
		this.data = data;
	}

	public static String error(String errorCode, String message) {
		BaseResponse base = new BaseResponse(false, errorCode, message, null);
		return base.toJson();
	}

	public static String success(String errorCode, String message, Object data) {
		BaseResponse base = new BaseResponse(true, Consts.SUCCESS, message, data);
		return base.toJson();
	}

	public static String success(Object data, long totalRecords) {
		BaseResponse base = new BaseResponse(true, Consts.SUCCESS, null, data, totalRecords);
		return base.toJson();
	}

	public static String success(Object data, long totalRecords, Object statistic) {
		BaseResponse base = new BaseResponse(true, Consts.SUCCESS, null, data, statistic, totalRecords);
		return base.toJson();
	}

	public String success(T data,String message) {
		BaseResponse base = new BaseResponse(true, Consts.SUCCESS,message, data);
		return base.toJson();
	}
	
	public String success(T data) {
		BaseResponse base = new BaseResponse(true, Consts.SUCCESS, null, data);
		return base.toJson();
	}


	public BaseResponse() {
		super();
		this.success = false;
		this.errorCode = null;
		this.message = "";
		this.data = null;
		this.totalRecords = 0;
	}
	

	public BaseResponse(String errorCode, String message) {
		super();
		this.success=false;
		this.errorCode = errorCode;
		this.message = message;
		this.totalRecords = 0;
	}
	public BaseResponse(String errorCode, String message , T data) {
		super();
		this.data=data;
		this.success=true;
		this.errorCode = errorCode;
		this.message = message;
		this.totalRecords = 0;
	}

	@Override
	public String toString() {
		return "BaseResponse{" +
				"success=" + success +
				", errorCode='" + errorCode + '\'' +
				", message='" + message + '\'' +
				", totalRecords=" + totalRecords + + '\'' +
				", statistic=" + statistic  + '\'' +
				", data=" + data  +
				'}';
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
