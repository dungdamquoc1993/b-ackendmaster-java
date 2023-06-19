package com.vinplay.dto.ibc2;


import java.io.Serializable;

public class GetReportTokenReqDto extends BaseReqDto implements Serializable {
	private static final long serialVersionUID = -4244376598961077360L;
	private String trans_id;
	private String user_language;
	
	public String getTrans_id() {
		return trans_id;
	}
	public void setTrans_id(String trans_id) {
		this.trans_id = trans_id;
	}
	public String getUser_language() {
		return user_language;
	}
	public void setUser_language(String user_language) {
		this.user_language = user_language;
	}
}
