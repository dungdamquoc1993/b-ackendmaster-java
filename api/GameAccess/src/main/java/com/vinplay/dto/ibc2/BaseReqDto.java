package com.vinplay.dto.ibc2;

import java.io.Serializable;

public class BaseReqDto implements Serializable {
	private String vendor_id;

	public String getVendor_id() {
		return vendor_id;
	}

	public void setVendor_id(String vendor_id) {
		this.vendor_id = vendor_id;
	}
}