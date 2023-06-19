package com.vinplay.dal.entities.fish;

public class FishTransaction {
	private String orderId;
	
	private String prefix;

	private String nickname;

	private String param;

	private Long timeStamp;

	private String action;

	private Long money;

	private String key;

	private String status;
	
	private String urlApi;
	
	public FishTransaction() {
		super();
	}
	
	public FishTransaction(String orderId, String prefix, String nickname, String param, Long timeStamp, String action,
			Long money, String key, String status, String urlApi) {
		super();
		this.orderId = orderId;
		this.prefix = prefix;
		this.nickname = nickname;
		this.param = param;
		this.timeStamp = timeStamp;
		this.action = action;
		this.money = money;
		this.key = key;
		this.status = status;
		this.urlApi = urlApi;
	}

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public String getPrefix() {
		return prefix;
	}

	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public String getParam() {
		return param;
	}

	public void setParam(String param) {
		this.param = param;
	}

	public Long getTimeStamp() {
		return timeStamp;
	}

	public void setTimeStamp(Long timeStamp) {
		this.timeStamp = timeStamp;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public Long getMoney() {
		return money;
	}

	public void setMoney(Long money) {
		this.money = money;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}
	
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	
	public String getUrlApi() {
		return urlApi;
	}

	public void setUrlApi(String urlApi) {
		this.urlApi = urlApi;
	}
}
