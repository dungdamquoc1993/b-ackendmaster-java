package com.vinplay.livecasino.api.core.obj;

import org.python.parser.ast.Str;

/**
 * <CODE> CreateRegisterPlayerApiRequest </CODE>
 * 创建/确认玩家接口请求物件
 * @author PHOENIX WU
 * @Version 1.0
 * @Date 2016年9月30日
 **/
public class FundTransactionOutAllApiRequest extends TCGBaseRequest {

	/**
	 * 玩家帐号
	 */
	private String username;

	/**
	 * 玩家密码
	 */
	private int product_type;

	private String reference_no;

	public FundTransactionOutAllApiRequest() {
		setMethod("ftoa");
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

	public String getReference_no() {
		return reference_no;
	}

	public void setReference_no(String reference_no) {
		this.reference_no = reference_no;
	}
}
