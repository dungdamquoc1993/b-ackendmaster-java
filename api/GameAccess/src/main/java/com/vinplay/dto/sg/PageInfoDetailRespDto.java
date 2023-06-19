package com.vinplay.dto.sg;

import java.io.Serializable;

@SuppressWarnings("serial")
public class PageInfoDetailRespDto implements Serializable{
	private Integer pageSize;
	private Integer pageNumber;
	private Integer totalCount;
	private Integer totalPage;
	private Integer firstRowNumber;
	private Integer lastRowNumber;
	public Integer getPageSize() {
		return pageSize;
	}
	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}
	public Integer getPageNumber() {
		return pageNumber;
	}
	public void setPageNumber(Integer pageNumber) {
		this.pageNumber = pageNumber;
	}
	public Integer getTotalCount() {
		return totalCount;
	}
	public void setTotalCount(Integer totalCount) {
		this.totalCount = totalCount;
	}
	public Integer getTotalPage() {
		return totalPage;
	}
	public void setTotalPage(Integer totalPage) {
		this.totalPage = totalPage;
	}
	public Integer getFirstRowNumber() {
		return firstRowNumber;
	}
	public void setFirstRowNumber(Integer firstRowNumber) {
		this.firstRowNumber = firstRowNumber;
	}
	public Integer getLastRowNumber() {
		return lastRowNumber;
	}
	public void setLastRowNumber(Integer lastRowNumber) {
		this.lastRowNumber = lastRowNumber;
	}
	
}
