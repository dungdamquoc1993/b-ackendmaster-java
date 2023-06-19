package com.vinplay.api.processors.dto;

import java.io.Serializable;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;

public class PaymentConfigUiDto implements Serializable {

	private String providerName;
	private ConfigUIDto providerConfig;

	public String getProviderName() {
		return providerName;
	}

	public void setProviderName(String providerName) {
		this.providerName = providerName;
	}

	public ConfigUIDto getProviderConfig() {
		return providerConfig;
	}

	public void setProviderConfig(ConfigUIDto providerConfig) {
		this.providerConfig = providerConfig;
	}

	public PaymentConfigUiDto(String providerName, ConfigUIDto providerConfig) {
		super();
		this.providerName = providerName;
		this.providerConfig = providerConfig;
	}

	public PaymentConfigUiDto() {
		super();
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

}
