package com.vinplay.dichvuthe.response;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
public class RechargePaywellResponse extends RechargeResponse {

	private String data;
	public String getData() {
		return data;
	}
	public void setData(String data) {
		this.data = data;
	}

	/**
	 * @param code
	 * @param currentMoney
	 * @param fail
	 * @param time
	 * @param data
	 */
	public RechargePaywellResponse(String data){
		super(1, 0, 0, 0);
		this.data = data;
	}
	public RechargePaywellResponse(int code, long currentMoney, int fail, long time, String data) {
		super(code, currentMoney, fail, time);
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
