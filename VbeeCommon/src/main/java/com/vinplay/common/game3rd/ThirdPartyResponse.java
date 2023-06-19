package com.vinplay.common.game3rd;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;

public class ThirdPartyResponse<T> {
	private long totalRecord;
	private long totalBet;
	private long totalValidBet;
	private long totalPayout;
	private long totalPlayer;
	private T listTrans;

	public ThirdPartyResponse() {
		super();
	}

	public ThirdPartyResponse(long totalRecord, long totalBet, long totalValidBet, long totalPayout, T listTrans) {
		super();
		this.totalRecord = totalRecord;
		this.totalBet = totalBet;
		this.totalValidBet = totalValidBet;
		this.totalPayout = totalPayout;
		this.listTrans = listTrans;
	}

	public ThirdPartyResponse(long totalRecord, long totalBet, long totalValidBet, long totalPayout, long totalPlayer, T listTrans) {
		this.totalRecord = totalRecord;
		this.totalBet = totalBet;
		this.totalValidBet = totalValidBet;
		this.totalPayout = totalPayout;
		this.totalPlayer = totalPlayer;
		this.listTrans = listTrans;
	}

	public long getTotalPlayer() {
		return totalPlayer;
	}

	public void setTotalPlayer(long totalPlayer) {
		this.totalPlayer = totalPlayer;
	}

	public long getTotalRecord() {
		return totalRecord;
	}

	public void setTotalRecord(long totalRecord) {
		this.totalRecord = totalRecord;
	}

	public long getTotalBet() {
		return totalBet;
	}

	public void setTotalBet(long totalBet) {
		this.totalBet = totalBet;
	}

	public long getTotalValidBet() {
		return totalValidBet;
	}

	public void setTotalValidBet(long totalValidBet) {
		this.totalValidBet = totalValidBet;
	}

	public long getTotalPayout() {
		return totalPayout;
	}

	public void setTotalPayout(long totalPayout) {
		this.totalPayout = totalPayout;
	}

	public T getListTrans() {
		return listTrans;
	}

	public void setListTrans(T listTrans) {
		this.listTrans = listTrans;
	}

	public String toJson() {
		ObjectWriter ow = new ObjectMapper().writer();
		ow.with(SerializationFeature.INDENT_OUTPUT);
		try {
			String json = ow.writeValueAsString(this);
			return json;
		} catch (Exception e) {
			return null;
		}
	}

}
