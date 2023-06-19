package com.vinplay.dto.ibc2;


import java.io.Serializable;

public class CheckUserBalanceReqDto extends BaseReqDto implements Serializable {

	private static final long serialVersionUID = -2676147206053303958L;
	private String vendor_member_ids;
	private Integer wallet_id;

	public String getVendor_member_ids() {
		return vendor_member_ids;
	}

	public void setVendor_member_ids(String vendor_member_ids) {
		this.vendor_member_ids = vendor_member_ids;
	}

	public Integer getWallet_id() {
		return wallet_id;
	}

	public void setWallet_id(Integer wallet_id) {
		this.wallet_id = wallet_id;
	}
}
