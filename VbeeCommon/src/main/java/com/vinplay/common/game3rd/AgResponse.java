package com.vinplay.common.game3rd;

import java.util.List;

public class AgResponse {
	private long TotalTrans;
	private long TotalMoney;
	private long TotalSuccess;
	private List<AGGameRecordItem> ListTrans;
	public long getTotalTrans() {
		return TotalTrans;
	}
	public void setTotalTrans(long totalTrans) {
		TotalTrans = totalTrans;
	}
	public long getTotalMoney() {
		return TotalMoney;
	}
	public void setTotalMoney(long totalMoney) {
		TotalMoney = totalMoney;
	}
	public long getTotalSuccess() {
		return TotalSuccess;
	}
	public void setTotalSuccess(long totalSuccess) {
		TotalSuccess = totalSuccess;
	}
	public List<AGGameRecordItem> getListTrans() {
		return ListTrans;
	}
	public void setListTrans(List<AGGameRecordItem> listTrans) {
		ListTrans = listTrans;
	}
	public AgResponse(long totalTrans, long totalMoney, long totalSuccess, List<AGGameRecordItem> listTrans) {
		super();
		TotalTrans = totalTrans;
		TotalMoney = totalMoney;
		TotalSuccess = totalSuccess;
		ListTrans = listTrans;
	}
	public AgResponse() {
		super();
	}
	
	
}
