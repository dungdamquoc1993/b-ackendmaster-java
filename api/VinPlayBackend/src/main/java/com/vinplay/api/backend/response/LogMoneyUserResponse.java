package com.vinplay.api.backend.response;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vinplay.vbee.common.response.BaseResponseModel;

public class LogMoneyUserResponse extends BaseResponseModel{
	private long totalData;
	private long totalBet;
	private long totalFee;
	private long totalSoVongcuoc;
    public LogMoneyUserResponse(boolean success, String errorCode) {
        super(success, errorCode);
    }

    public LogMoneyUserResponse(boolean success, String errorCode, Object data) {
        super(success, errorCode, data);
    }

    public LogMoneyUserResponse(boolean success, String errorCode, int totalData) {
        super(success, errorCode);
        this.totalData = totalData;
    }

    public LogMoneyUserResponse(boolean success, String errorCode, Object data, int totalData) {
        super(success, errorCode, data);
        this.totalData = totalData;
    }

    public long getTotalData() {
        return totalData;
    }

    public void setTotalData(long totalData) {
        this.totalData = totalData;
    }

    public LogMoneyUserResponse(boolean success, String errorCode, long totalData, long totalBet, long totalFee) {
		super(success, errorCode);
		this.totalData = totalData;
		this.totalBet = totalBet;
		this.totalFee = totalFee;
	}

	public long getTotalBet() {
		return totalBet;
	}

	public void setTotalBet(long totalBet) {
		this.totalBet = totalBet;
	}

	public long getTotalFee() {
		return totalFee;
	}

	public void setTotalFee(long totalFee) {
		this.totalFee = totalFee;
	}

	public long getTotalSoVongcuoc() {
		return totalSoVongcuoc;
	}

	public void setTotalSoVongcuoc(long totalSoVongcuoc) {
		this.totalSoVongcuoc = totalSoVongcuoc;
	}

	@Override
    public String toJson() {
        try {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.writeValueAsString((Object)this);
        }
        catch (JsonProcessingException mapper) {
            return "{\"success\":false,\"errorCode\":\"1001\",\"totalData\":\"0\"}";
        }
    }
}
