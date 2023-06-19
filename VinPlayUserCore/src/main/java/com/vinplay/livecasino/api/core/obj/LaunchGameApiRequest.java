	package com.vinplay.livecasino.api.core.obj;

/**
 * <CODE> CreateRegisterPlayerApiRequest </CODE>
 * 创建/确认玩家接口请求物件
 * @author PHOENIX WU
 * @Version 1.0
 * @Date 2016年9月30日
 **/
public class LaunchGameApiRequest extends TCGBaseRequest {

	/**
	 * 玩家帐号
	 */
	private String username;

	/**
	 * 玩家密码
	 */
	private int product_type;

	private String platform;

	private String game_mode;

	private String game_code;

	private String language;

	public LaunchGameApiRequest() {
		setMethod("lg");
		setLanguage("VI");
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

	public String getPlatform() {
		return platform;
	}

	public void setPlatform(String platform) {
		this.platform = platform;
	}

	public String getGame_mode() {
		return game_mode;
	}

	public void setGame_mode(String game_mode) {
		this.game_mode = game_mode;
	}

	public String getGame_code() {
		return game_code;
	}

	public void setGame_code(String game_code) {
		this.game_code = game_code;
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}
}
