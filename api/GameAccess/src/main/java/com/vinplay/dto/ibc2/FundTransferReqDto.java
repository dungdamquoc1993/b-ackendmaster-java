package com.vinplay.dto.ibc2;


import java.io.Serializable;

public class FundTransferReqDto extends BaseReqDto implements Serializable {
	private static final long serialVersionUID = -9025097638530616727L;
	private String vendor_member_id;
	private String vendor_trans_id;
	private Double amount;
	private Integer currency;
	private Integer direction;
	private Integer wallet_id;
	
	public String getVendor_member_id() {
		return vendor_member_id;
	}
	public void setVendor_member_id(String vendor_member_id) {
		this.vendor_member_id = vendor_member_id;
	}
	public String getVendor_trans_id() {
		return vendor_trans_id;
	}
	public void setVendor_trans_id(String vendor_trans_id) {
		this.vendor_trans_id = vendor_trans_id;
	}
	public Double getAmount() {
		return amount;
	}
	public void setAmount(Double amount) {
		this.amount = amount;
	}
	public Integer getCurrency() {
		return currency;
	}
	public void setCurrency(Integer currency) {
		this.currency = currency;
	}
	public Integer getDirection() {
		return direction;
	}
	public void setDirection(Integer direction) {
		this.direction = direction;
	}
	public Integer getWallet_id() {
		return wallet_id;
	}
	public void setWallet_id(Integer wallet_id) {
		this.wallet_id = wallet_id;
	}
	
}
