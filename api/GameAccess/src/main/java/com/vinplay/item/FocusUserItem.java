package com.vinplay.item;

import java.io.Serializable;

public class FocusUserItem implements Serializable{

	private static final long serialVersionUID = 7334923931370067751L;

	private String loginname;
	
	private String focusflag;

	public String getLoginname() {
		return loginname;
	}

	public void setLoginname(String loginname) {
		this.loginname = loginname;
	}

	public String getFocusflag() {
		return focusflag;
	}

	public void setFocusflag(String focusflag) {
		this.focusflag = focusflag;
	}

}
