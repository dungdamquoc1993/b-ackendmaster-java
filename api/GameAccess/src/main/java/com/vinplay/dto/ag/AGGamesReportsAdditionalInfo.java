package com.vinplay.dto.ag;

import java.io.Serializable;

public class AGGamesReportsAdditionalInfo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7072090899708059696L;

	private String total;
	private String num_per_page;
	private String currentpage;
	private String totalpage;
	private String perpage;
	
	public String getTotal() {
		return total;
	}
	
	public void setTotal(String total) {
		this.total = total;
	}
	
	public String getNum_per_page() {
		return num_per_page;
	}
	
	public void setNum_per_page(String num_per_page) {
		this.num_per_page = num_per_page;
	}
	
	public String getCurrentpage() {
		return currentpage;
	}

	public void setCurrentpage(String currentpage) {
		this.currentpage = currentpage;
	}

	public String getTotalpage() {
		return totalpage;
	}

	public void setTotalpage(String totalpage) {
		this.totalpage = totalpage;
	}

	public String getPerpage() {
		return perpage;
	}

	public void setPerpage(String perpage) {
		this.perpage = perpage;
	}
}
