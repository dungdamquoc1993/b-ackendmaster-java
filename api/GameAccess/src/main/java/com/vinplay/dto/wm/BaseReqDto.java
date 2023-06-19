package com.vinplay.dto.wm;

import java.io.Serializable;

@SuppressWarnings("serial")
public class BaseReqDto implements Serializable {
	private String cmd;
	private String vendorId;
	private String signature;

	public String getCmd() {
		return cmd;
	}

	public void setCmd(String cmd) {
		this.cmd = cmd;
	}

	public String getVendorId() {
		return vendorId;
	}

	public void setVendorId(String vendorId) {
		this.vendorId = vendorId;
	}

	public String getSignature() {
		return signature;
	}

	public void setSignature(String signature) {
		this.signature = signature;
	}

}