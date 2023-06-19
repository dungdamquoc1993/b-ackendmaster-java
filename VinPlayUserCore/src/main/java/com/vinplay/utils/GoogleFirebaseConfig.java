package com.vinplay.utils;

public class GoogleFirebaseConfig {
	
	/**
	 * @param serverKey
	 * @param browerKey
	 * @param urlSendCode
	 * @param urlVerifyPhone
	 */
	public GoogleFirebaseConfig(String serverKey, String browerKey, String urlSendCode, String urlVerifyPhone) {
		super();
		this.serverKey = serverKey;
		this.browerKey = browerKey;
		this.urlSendCode = urlSendCode;
		this.urlVerifyPhone = urlVerifyPhone;
	}
	
	public String serverKey;
	public String browerKey;
	public String urlSendCode;
	public String urlVerifyPhone;
}
