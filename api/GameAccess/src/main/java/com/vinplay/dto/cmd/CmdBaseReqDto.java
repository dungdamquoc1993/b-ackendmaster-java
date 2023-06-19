package com.vinplay.dto.cmd;

import java.io.Serializable;


public class CmdBaseReqDto implements Serializable {

	private static final long serialVersionUID = -6635885782630973284L;

	private String PartnerKey;
	private String CurrencyCode;

	public String getPartnerKey() {
		return PartnerKey;
	}

	public void setPartnerKey(String partnerKey) {
		PartnerKey = partnerKey;
	}

	public String getCurrencyCode() {
		return CurrencyCode;
	}

	public void setCurrencyCode(String currencyCode) {
		CurrencyCode = currencyCode;
	}

}
