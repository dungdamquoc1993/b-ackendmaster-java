package com.vinplay.dto.ibc2;


import java.io.Serializable;

public class GetFinancialReportReqDto extends BaseReqDto implements Serializable {
	private static final long serialVersionUID = -2698542112368787456L;
	private String vendor_id;
	private String FinancialDate;
	private Integer Currency;
	
	public String getVendor_id() {
		return vendor_id;
	}
	public void setVendor_id(String vendor_id) {
		this.vendor_id = vendor_id;
	}
	public String getFinancialDate() {
		return FinancialDate;
	}
	public void setFinancialDate(String financialDate) {
		FinancialDate = financialDate;
	}
	public Integer getCurrency() {
		return Currency;
	}
	public void setCurrency(Integer currency) {
		Currency = currency;
	}

}
