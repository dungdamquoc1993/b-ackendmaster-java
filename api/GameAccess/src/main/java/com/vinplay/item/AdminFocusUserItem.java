package com.vinplay.item;

import java.io.Serializable;
/**
 * 
 * 后台用户关注客户
 * @author Owner
 *
 */
public class AdminFocusUserItem implements Serializable{

	private static final long serialVersionUID = 8502414072427310157L;

	private String loginname;
	
	private String adminuser;

	private String focustime;
	
	private String remark;

	public String getLoginname() {
		return loginname;
	}

	public void setLoginname(String loginname) {
		this.loginname = loginname;
	}

	public String getAdminuser() {
		return adminuser;
	}

	public void setAdminuser(String adminuser) {
		this.adminuser = adminuser;
	}

	public String getFocustime() {
		return focustime;
	}

	public void setFocustime(String focustime) {
		this.focustime = focustime;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}
}
