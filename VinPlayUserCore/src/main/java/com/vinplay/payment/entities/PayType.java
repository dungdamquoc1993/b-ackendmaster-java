package com.vinplay.payment.entities;

public class PayType {
	/**
	 * @param key
	 * @param name
	 * @param status
	 */
	public PayType(String key, String name, Integer status) {
		super();
		this.key = key;
		this.name = name;
		this.status = status;
	}
	
	public String key;
	public String name;
	public Integer status;
}
