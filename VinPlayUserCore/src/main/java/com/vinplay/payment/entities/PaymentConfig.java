package com.vinplay.payment.entities;

public class PaymentConfig {

	private String name;
	private Config config;

	/**
	 * @param name
	 * @param config
	 */
	public PaymentConfig(String name, Config config) {
		super();
		this.name = name;
		this.config = config;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Config getConfig() {
		return config;
	}

	public void setConfig(Config config) {
		this.config = config;
	}

	public PaymentConfig() {
	}

	@Override
	public String toString() {
		return "PaymentConfig [name=" + name + ", config=" + config + "]";
	}
}
