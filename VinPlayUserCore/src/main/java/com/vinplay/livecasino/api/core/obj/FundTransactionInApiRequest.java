package com.vinplay.livecasino.api.core.obj;

/**
 * <CODE> CreateRegisterPlayerApiRequest </CODE>
 * 创建/确认玩家接口请求物件
 * @author PHOENIX WU
 * @Version 1.0
 * @Date 2016年9月30日
 **/
public class FundTransactionInApiRequest extends TCGBaseRequest {

	/**
	 * 玩家帐号
	 */
	private String username;

	/**
	 * 玩家密码
	 */
	private int product_type;

	private String fund_type;

	private double amount;

	private String reference_no;

	public FundTransactionInApiRequest() {
		setMethod("ft");
		setFund_type("1");
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public int getProduct_type() {
		return product_type;
	}

	public void setProduct_type(int product_type) {
		this.product_type = product_type;
	}

	public String getFund_type() {
		return fund_type;
	}

	public void setFund_type(String fund_type) {
		this.fund_type = fund_type;
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	public String getReference_no() {
		return reference_no;
	}

	public void setReference_no(String reference_no) {
		this.reference_no = reference_no;
	}
}
