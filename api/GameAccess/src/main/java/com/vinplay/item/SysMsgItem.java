package com.vinplay.item;

public class SysMsgItem implements java.io.Serializable{

	/**
	 *
	 */
	private static final long serialVersionUID = 248777655330665565L;
	private Integer id;
	private String title;
	private String content;
	private String savetime;
	private String insertby;
	private Integer status;
	private String ip;
	private String deleteby;
	private Integer param1;
	private String param2;
	private Integer param3;
	private String photourl;
	private String contentcode;
	
	private String begins;
	private String ends;

	public String getPhotourl() {
		return photourl;
	}
	public void setPhotourl(String photourl) {
		this.photourl = photourl;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getSavetime() {
		return savetime;
	}
	public void setSavetime(String savetime) {
		this.savetime = savetime;
	}
	public String getInsertby() {
		return insertby;
	}
	public void setInsertby(String insertby) {
		this.insertby = insertby;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	public String getDeleteby() {
		return deleteby;
	}
	public void setDeleteby(String deleteby) {
		this.deleteby = deleteby;
	}
	public Integer getParam1() {
		return param1;
	}
	public void setParam1(Integer param1) {
		this.param1 = param1;
	}
	public String getParam2() {
		return param2;
	}
	public void setParam2(String param2) {
		this.param2 = param2;
	}
	public Integer getParam3() {
		return param3;
	}
	public void setParam3(Integer param3) {
		this.param3 = param3;
	}
	public String getContentcode() {
		return contentcode;
	}
	public void setContentcode(String contentcode) {
		this.contentcode = contentcode;
	}
	public String getBegins() {
		return begins;
	}
	public void setBegins(String begins) {
		this.begins = begins;
	}
	public String getEnds() {
		return ends;
	}
	public void setEnds(String ends) {
		this.ends = ends;
	}

}
