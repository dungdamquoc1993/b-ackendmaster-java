package com.vinplay.item;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class BuyMutilBetting implements Serializable {
	private static final long serialVersionUID = -1611444445874089826L;
	private String type;
	private int lotteryid;
	private String issue_start;
	private String gamerecordid;
	private int total_nums;
	private double total_money;
	//是否追号
	private String trace_if="no";
	private String trace_stop="no";
	private int trace_count_input;
	private double trace_money;
	private List<BettingItem> detaillist = new ArrayList<>();
	private List<BuyTraceItem> tracelist = new ArrayList<>();
	public void addDetailitem(BettingItem bdi){
		detaillist.add(bdi);
	}
	public void addTraceItem(BuyTraceItem bti){
		tracelist.add(bti);
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public int getLotteryid() {
		return lotteryid;
	}

	public void setLotteryid(int lotteryid) {
		this.lotteryid = lotteryid;
	}

	public String getIssue_start() {
		return issue_start;
	}

	public void setIssue_start(String issue_start) {
		this.issue_start = issue_start;
	}

	public String getGamerecordid() {
		return gamerecordid;
	}

	public void setGamerecordid(String gamerecordid) {
		this.gamerecordid = gamerecordid;
	}

	public int getTotal_nums() {
		return total_nums;
	}

	public void setTotal_nums(int total_nums) {
		this.total_nums = total_nums;
	}

	public double getTotal_money() {
		return total_money;
	}

	public void setTotal_money(double total_money) {
		this.total_money = total_money;
	}

	public List<BettingItem> getDetaillist() {
		return detaillist;
	}

	public void setDetaillist(List<BettingItem> detaillist) {
		this.detaillist = detaillist;
	}

	public String getTrace_if() {
		return trace_if;
	}

	public void setTrace_if(String trace_if) {
		this.trace_if = trace_if;
	}

	public String getTrace_stop() {
		return trace_stop;
	}

	public void setTrace_stop(String trace_stop) {
		this.trace_stop = trace_stop;
	}

	public int getTrace_count_input() {
		return trace_count_input;
	}

	public void setTrace_count_input(int trace_count_input) {
		this.trace_count_input = trace_count_input;
	}

	public double getTrace_money() {
		return trace_money;
	}

	public void setTrace_money(double trace_money) {
		this.trace_money = trace_money;
	}

	public List<BuyTraceItem> getTracelist() {
		return tracelist;
	}

	public void setTracelist(List<BuyTraceItem> tracelist) {
		this.tracelist = tracelist;
	}
}
