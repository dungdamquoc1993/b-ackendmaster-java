package com.vinplay.dto.ebet;

import java.io.Serializable;

public class WalletDto implements Serializable{
	private static final long serialVersionUID = -6324908643029200904L;
	private Integer typeId;
	private Double money;
	public Integer getTypeId() {
		return typeId;
	}
	public void setTypeId(Integer typeId) {
		this.typeId = typeId;
	}
	public Double getMoney() {
		return money;
	}
	public void setMoney(Double money) {
		this.money = money;
	}

}
