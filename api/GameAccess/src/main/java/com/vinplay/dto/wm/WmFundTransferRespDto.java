package com.vinplay.dto.wm;

@SuppressWarnings("serial")
public class WmFundTransferRespDto extends BaseReqDto {

	private String user;
	private Double money;
	private String order;
	private Integer syslang;

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public Double getMoney() {
		return money;
	}

	public void setMoney(Double money) {
		this.money = money;
	}

	public String getOrder() {
		return order;
	}

	public void setOrder(String order) {
		this.order = order;
	}

	public Integer getSyslang() {
		return syslang;
	}

	public void setSyslang(Integer syslang) {
		this.syslang = syslang;
	}

}
