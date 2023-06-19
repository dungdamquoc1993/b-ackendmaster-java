package com.vinplay.item;

import java.io.Serializable;

/**
 * 
 * 权限明细
 * 
 * @author Donny
 *
 */
public class AuthorityItem implements Serializable{

	private static final long serialVersionUID = -598915674314120644L;

	/**
	 * 权限id
	 */
	private Integer authorityid;
	
	/**
	 * 权限对应的类型
	 */
	private Integer authoritytype;
	
	/**
	 * 类型下的id
	 */
	private Integer authoritynum;
	
	/**
	 * 权限名
	 */
	private String authorityname;
	
	/**
	 * 备注
	 */
	private String remark;


	public Integer getAuthorityid() {
		return authorityid;
	}

	public void setAuthorityid(Integer authorityid) {
		this.authorityid = authorityid;
	}

	public Integer getAuthoritytype() {
		return authoritytype;
	}

	public void setAuthoritytype(Integer authoritytype) {
		this.authoritytype = authoritytype;
	}

	public Integer getAuthoritynum() {
		return authoritynum;
	}

	public void setAuthoritynum(Integer authoritynum) {
		this.authoritynum = authoritynum;
	}

	public String getAuthorityname() {
		return authorityname;
	}

	public void setAuthorityname(String authorityname) {
		this.authorityname = authorityname;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}
}
