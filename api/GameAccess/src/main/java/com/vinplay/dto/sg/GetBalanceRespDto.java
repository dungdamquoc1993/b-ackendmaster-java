package com.vinplay.dto.sg;

@SuppressWarnings("serial")
public class GetBalanceRespDto extends BaseRespDto{
	private Integer count;
	private String querytime;
	private Object results;
	public Integer getCount() {
		return count;
	}
	public void setCount(Integer count) {
		this.count = count;
	}
	public String getQuerytime() {
		return querytime;
	}
	public void setQuerytime(String querytime) {
		this.querytime = querytime;
	}
	public Object getResults() {
		return results;
	}
	public void setResults(Object results) {
		this.results = results;
	}
	
	
}
