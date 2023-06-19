/**
 * Archie
 */
package com.vinplay.dto;

/**
 * @author Archie
 *
 */
public class FundTransferHistoryDto {
	private String loginname;
	private double amount;
	private int direction;
	private String wid;
	private int thirdparty_id;
	private double thirdparty_amount;
	private String message;

	public String getLoginname() {
		return loginname;
	}

	public void setLoginname(String loginname) {
		this.loginname = loginname;
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	public int getDirection() {
		return direction;
	}

	public void setDirection(int direction) {
		this.direction = direction;
	}

	public String getWid() {
		return wid;
	}

	public void setWid(String wid) {
		this.wid = wid;
	}

	public int getThirdparty_id() {
		return thirdparty_id;
	}

	public void setThirdparty_id(int thirdparty_id) {
		this.thirdparty_id = thirdparty_id;
	}

	public double getThirdparty_amount() {
		return thirdparty_amount;
	}

	public void setThirdparty_amount(double thirdparty_amount) {
		this.thirdparty_amount = thirdparty_amount;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public FundTransferHistoryDto(String loginname, double amount, int direction, String wid, int thirdparty_id,
			double thirdparty_amount, String message) {
		super();
		this.loginname = loginname;
		this.amount = amount;
		this.direction = direction;
		this.wid = wid;
		this.thirdparty_id = thirdparty_id;
		this.thirdparty_amount = thirdparty_amount;
		this.message = message;
	}

	public FundTransferHistoryDto() {
		super();
	}

}
