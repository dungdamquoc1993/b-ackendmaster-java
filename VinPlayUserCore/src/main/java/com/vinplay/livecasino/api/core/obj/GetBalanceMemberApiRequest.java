package com.vinplay.livecasino.api.core.obj;

/**
 * <CODE> CreateRegisterPlayerApiRequest </CODE>
 * 创建/确认玩家接口请求物件
 * @author PHOENIX WU
 * @Version 1.0
 * @Date 2016年9月30日
 **/
public class GetBalanceMemberApiRequest extends TCGBaseRequest {

	/**
	 * 玩家帐号
	 */
	private String username;

	/**
	 * 玩家密码
	 */
	private int product_type;

	public GetBalanceMemberApiRequest() {
		setMethod("gb");
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
}
