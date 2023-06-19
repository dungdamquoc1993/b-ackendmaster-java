package com.vinplay.item;

import java.io.Serializable;

/**
 * 秒秒彩比例优化item
 * 
 * @author Donny
 *
 */
public class AdvMMCUserItem implements Serializable{

	private static final long serialVersionUID = 6237698487624337430L;

	/**
	 * 用户名
	 */
	private String name;
	
	/**
	 * 优化比例
	 */
	private Integer area=0;
	/**
	 * 彩种ID
	 */
	private Integer lotteryid=50;
	/**
	 * 添加时间
	 */
	private String addtime;
	
	private String addby;
	private String addip;
	private Integer status;
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getArea() {
		return area;
	}

	public void setArea(Integer area) {
		this.area = area;
	}

	public Integer getLotteryid() {
		return lotteryid;
	}

	public void setLotteryid(Integer lotteryid) {
		this.lotteryid = lotteryid;
	}

	public String getAddtime() {
		return addtime;
	}

	public void setAddtime(String addtime) {
		this.addtime = addtime;
	}

	public String getAddby() {
		return addby;
	}

	public void setAddby(String addby) {
		this.addby = addby;
	}

	public String getAddip() {
		return addip;
	}

	public void setAddip(String addip) {
		this.addip = addip;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}
}
