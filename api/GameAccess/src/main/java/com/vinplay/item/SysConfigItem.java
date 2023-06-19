package com.vinplay.item;

public class SysConfigItem implements java.io.Serializable{
	private static final long serialVersionUID = 8143295010463256377L;
	private String sname;
	private Integer valuen;
	private String svalue;
	private String slanguage;
	private String sgroup;
	private String remark;
	private String starttime;
	private String endtime;
	private String updatetime;
	private String updateby;
	private int transfer=0;//1 is ON, 0 is OFF

	public String getStarttime() {
		return starttime;
	}

	public void setStarttime(String starttime) {
		this.starttime = starttime;
	}

	public String getEndtime() {
		return endtime;
	}

	public void setEndtime(String endtime) {
		this.endtime = endtime;
	}

	public String getSname() {
		return sname;
	}
	public void setSname(String sname) {
		this.sname = sname;
	}
	public Integer getValuen() {
		return valuen;
	}
	public void setValuen(Integer valuen) {
		this.valuen = valuen;
	}
	public String getSvalue() {
		return svalue;
	}
	public void setSvalue(String svalue) {
		this.svalue = svalue;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getSlanguage() {
		return slanguage;
	}

	public void setSlanguage(String slanguage) {
		this.slanguage = slanguage;
	}

	public String getSgroup() {
		return sgroup;
	}

	public void setSgroup(String sgroup) {
		this.sgroup = sgroup;
	}

	public String getUpdatetime() {
		return updatetime;
	}

	public void setUpdatetime(String updatetime) {
		this.updatetime = updatetime;
	}

	public String getUpdateby() {
		return updateby;
	}

	public void setUpdateby(String updateby) {
		this.updateby = updateby;
	}

	public int getTransfer() {
		return transfer;
	}

	public void setTransfer(int transfer) {
		this.transfer = transfer;
	}
}
