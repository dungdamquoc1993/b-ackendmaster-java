package com.vinplay.dto.wm;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class GetDateTimeReportResultList implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7526550557957667094L;

	private List<GetDateTimeReportResult> data = new ArrayList<GetDateTimeReportResult>();

	public List<GetDateTimeReportResult> getData() {
		return data;
	}

	public void setData(List<GetDateTimeReportResult> data) {
		this.data = data;
	}
	
}
