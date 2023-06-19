package com.vinplay.item;

public class BaseReponse implements java.io.Serializable {
	private static final long serialVersionUID = 1L;

	private String msg;
	private String code;

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

}
