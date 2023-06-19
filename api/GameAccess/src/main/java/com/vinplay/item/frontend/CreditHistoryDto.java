/**
 * Archie
 */
package com.vinplay.item.frontend;

import java.io.Serializable;

import com.vinplay.item.AdjustmentItem;
import com.vinplay.item.TransferItem;

/**
 * @author Archie
 *
 */
public class CreditHistoryDto implements Serializable {
	private String loginname;
	private Double amount;
	private int status;
	private String transferTime;

	public CreditHistoryDto() {
	}

	public CreditHistoryDto(String loginname, Double amount, int status, String actiontime) {
		this.loginname = loginname;
		this.amount = amount;
		this.status = status;
		this.transferTime = actiontime;
	}

	public CreditHistoryDto(AdjustmentItem adjust) {
		this.loginname = adjust.getLastuser();
		this.amount = adjust.getAmount();
		this.status = adjust.getStatus();
		this.transferTime = adjust.getOpttime();
	}

	public CreditHistoryDto(TransferItem adjust) {
		this.loginname = adjust.getWid();
		this.amount = adjust.getAmount();
		this.status = adjust.getStatus() == 2 ? 4 : adjust.getStatus();
		this.transferTime = adjust.getTransfertime();
	}

	public String getLoginname() {
		return loginname;
	}

	public void setLoginname(String loginname) {
		this.loginname = loginname;
	}

	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getTransferTime() {
		return transferTime;
	}

	public void setTransferTime(String transferTime) {
		this.transferTime = transferTime;
	}

	@Override
	public String toString() {
		return "CreditHistoryDto [loginname=" + loginname + ", amount=" + amount + ", status=" + status
				+ ", actiontime=" + transferTime + "]";
	}

}
