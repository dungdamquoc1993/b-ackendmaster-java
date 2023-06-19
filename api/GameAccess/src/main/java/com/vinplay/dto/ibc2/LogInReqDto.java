package com.vinplay.dto.ibc2;


import java.io.Serializable;

public class LogInReqDto extends BaseReqDto implements Serializable {
	private static final long serialVersionUID = 8156157736659558446L;
	private String domain;
	private String vendor_member_id;
	
	public String getDomain() {
		return domain;
	}
	public void setDomain(String domain) {
		this.domain = domain;
	}
	public String getVendor_member_id() {
		return vendor_member_id;
	}
	public void setVendor_member_id(String vendor_member_id) {
		this.vendor_member_id = vendor_member_id;
	}

}
