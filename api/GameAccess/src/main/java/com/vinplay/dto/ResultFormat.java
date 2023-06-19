package com.vinplay.dto;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.vinplay.utils.Constants;

public class ResultFormat {
	private int res = 1;
	private String msg = "";
	private List<Object> list = new ArrayList<Object>();
	private Object data;
	
	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}

	public int getRes() {
		return res;
	}

	public void setRes(int res) {
		this.res = res;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public List<Object> getList() {
		return list;
	}

	public void setList(List<Object> list) {
		this.list = list;
	}

	public ResultFormat(int res, String msg, List<Object> list) {
		this.res = res;
		this.msg = msg;
		this.list = list;
	}
	public ResultFormat(int res, String msg, Object data) {
		this.res = res;
		this.msg = msg;
		this.data = data;
	}
	public ResultFormat(int res, String msg) {
		this.res = res;
		this.msg = msg;
		this.list = new ArrayList<Object>();
	}
	
	/**
	 * @param msg Message desc error
	 * @param res always = 1
	 */
	public ResultFormat(String msg) {
		this.res = Constants.FAIL;
		this.msg = msg;
	}
	public ResultFormat() {
	}

	@Override
	public String toString() {
		return "ResultFormat [res=" + res + ", msg=" + msg + ", list=" + list + "]";
	}
	public static void main(String[] args) {
		ResultFormat r = new ResultFormat();
		System.out.println(r.toJson());
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
