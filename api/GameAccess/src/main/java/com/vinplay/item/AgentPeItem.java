package com.vinplay.item;

public class AgentPeItem implements java.io.Serializable{
	private static final long serialVersionUID = 4452755833588517951L;
	private String loginname;
	private Double levels;
	private Integer num=0;
	private Integer num2=0;
	public String getLoginname() {
		return loginname;
	}
	public void setLoginname(String loginname) {
		this.loginname = loginname;
	}
	public Double getLevels() {
		return levels;
	}
	public void setLevels(Double levels) {
		this.levels = levels;
	}
	public Integer getNum() {
		return num;
	}
	public void setNum(Integer num) {
		this.num = num;
	}
	public Integer getNum2() {
		return num2;
	}
	public void setNum2(Integer num2) {
		this.num2 = num2;
	}
}
