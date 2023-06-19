package com.vinplay.item;

public class AdminDepositCodeItem implements java.io.Serializable{
	private static final long serialVersionUID = -8482165984429355484L;
	private Integer id;
	private String code;
	private int active;
	private String createdate;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public int getActive() {
		return active;
	}

	public void setActive(int active) {
		this.active = active;
	}

	public String getCreatedate() {
		return createdate;
	}

	public void setCreatedate(String createdate) {
		this.createdate = createdate;
	}
}
