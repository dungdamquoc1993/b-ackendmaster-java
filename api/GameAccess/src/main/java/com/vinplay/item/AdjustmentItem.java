package com.vinplay.item;

public class AdjustmentItem implements java.io.Serializable{

	private static final long serialVersionUID = -7470134827066372437L;
	private String loginname;
	private Integer types;
	private String typename;
	
	private String method;
	private Double amount;
	private String wid;
	private String id;
	private Integer status;
	private String createtime;
	private String opttime;
	private Double require=0.0;
	private String remark;
	private String lastuser;
	private String adminuser1=null;
	private String adminuser2=null;
	public String getLoginname() {
		return loginname;
	}
	public void setLoginname(String loginname) {
		this.loginname = loginname;
	}
	public Integer getTypes() {
		return types;
	}
	public void setTypes(Integer types) {
		this.types = types;
	}
	public String getMethod() {
		return method;
	}
	public void setMethod(String method) {
		this.method = method;
	}
	public Double getAmount() {
		return amount;
	}
	public void setAmount(Double amount) {
		this.amount = amount;
	}
	public String getWid() {
		return wid;
	}
	public void setWid(String wid) {
		this.wid = wid;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public String getOpttime() {
		return opttime;
	}
	public void setOpttime(String opttime) {
		this.opttime = opttime;
	}
	public String getTypename() {
		return typename;
	}
	public void setTypename(String typename) {
		this.typename = typename;
	}
	public Double getRequire() {
		return require;
	}
	public void setRequire(Double require) {
		this.require = require;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getAdminuser1() {
		return adminuser1;
	}
	public void setAdminuser1(String adminuser1) {
		this.adminuser1 = adminuser1;
	}
	public String getAdminuser2() {
		return adminuser2;
	}
	public void setAdminuser2(String adminuser2) {
		this.adminuser2 = adminuser2;
	}
	public String getCreatetime() {
		return createtime;
	}
	public void setCreatetime(String createtime) {
		this.createtime = createtime;
	}
	public String getLastuser() {
		return lastuser;
	}
	public void setLastuser(String lastuser) {
		this.lastuser = lastuser;
	}
	
}
