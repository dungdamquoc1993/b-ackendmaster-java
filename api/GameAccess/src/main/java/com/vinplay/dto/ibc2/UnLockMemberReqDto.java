package com.vinplay.dto.ibc2;


import java.io.Serializable;

public class UnLockMemberReqDto extends BaseReqDto implements Serializable {
	private static final long serialVersionUID = 500005596239516074L;
	private String vendor_member_id;

	public String getVendor_member_id() {
		return vendor_member_id;
	}

	public void setVendor_member_id(String vendor_member_id) {
		this.vendor_member_id = vendor_member_id;
	}
	
}
