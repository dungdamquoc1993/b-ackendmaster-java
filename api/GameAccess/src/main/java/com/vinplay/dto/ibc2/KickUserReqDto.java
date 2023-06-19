package com.vinplay.dto.ibc2;


import java.io.Serializable;

public class KickUserReqDto extends BaseReqDto implements Serializable {
	private static final long serialVersionUID = 2123358576384651148L;
	private String vendor_member_id;

	public String getVendor_member_id() {
		return vendor_member_id;
	}

	public void setVendor_member_id(String vendor_member_id) {
		this.vendor_member_id = vendor_member_id;
	}

	
}
