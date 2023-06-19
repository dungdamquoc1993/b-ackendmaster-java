package com.vinplay.dto.ebet;

import java.io.Serializable;

public class UserMoneyRespDto extends BaseRespDto implements Serializable{
	private static final long serialVersionUID = -9077940352808215421L;
	private ResultsDto[] results;

	public ResultsDto[] getResults() {
		return results;
	}

	public void setResults(ResultsDto[] results) {
		this.results = results;
	}
	
}
