package com.vinplay.dto.ibc2;


import java.io.Serializable;

public class CheckFundTransferReqDto extends BaseReqDto implements Serializable {
	private static final long serialVersionUID = 6888383459318775465L;
	private String vendor_trans_id;
	private Integer wallet_id;
	
	public String getVendor_trans_id() {
		return vendor_trans_id;
	}
	public void setVendor_trans_id(String vendor_trans_id) {
		this.vendor_trans_id = vendor_trans_id;
	}
	public Integer getWallet_id() {
		return wallet_id;
	}
	public void setWallet_id(Integer wallet_id) {
		this.wallet_id = wallet_id;
	}
}
