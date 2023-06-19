package com.vinplay.dal.entities.taixiu;

import java.io.Serializable;

public class ResultTaiXiu implements Serializable {
	private static final long serialVersionUID = 1L;
	public long referenceId;
	public int result;
	public int dice1;
	public int dice2;
	public int dice3;
	public long totalTai = 0L;
	public long totalXiu = 0L;
	public int numBetTai = 0;
	public int numBetXiu = 0;
	public long totalPrize = 0L;
	public long totalJp = 0L;
	public long totalRefundTai = 0L;
	public long totalRefundXiu = 0L;
	public long totalRevenue = 0L;
	public int moneyType;
	public String timestamp;
	public String md5 = "";
    public String before_md5 = "";

	public ResultTaiXiu() {
	}

}
