package com.vinplay.dto.sg;

import java.io.Serializable;

public class BaseSGRequest implements Serializable {
	private static final long serialVersionUID = 1L;
	private String cert;
	private String agentId;

	public String getCert() {
		return cert;
	}

	public void setCert(String cert) {
		this.cert = cert;
	}

	public String getAgentId() {
		return agentId;
	}

	public void setAgentId(String agentId) {
		this.agentId = agentId;
	}

}
