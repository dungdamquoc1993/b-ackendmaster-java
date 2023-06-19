package com.vinplay.payment.utils;

import java.util.Arrays;
import java.util.List;

public interface PaymentConstant {
	
	public interface PROVIDER {
		String PAYWELL = "paywell";
		String PRINCE_PAY = "princepay";
		String ROYAL_PAY = "royalpay";
		String CLICK_PAY = "clickpay";
		String MANUAL_BANK = "manualbank";
		String SC = "SC";
	}
	public interface PAYWELL {
		String PAY_ONLINE = "IB_ONLINE";
		String PAY_OFFLINE = "PW_OFFLINE";
	}

	public interface PRINCEPAY {
		int SILVER_PAY = 907;
		int BANK_TRANS = 908;
		int ZALO_PAY = 921;
		int MOMO_PAY = 923;
		int VIETNAM_PAY = 712;
		List<Integer> PAY_TYPE = Arrays.asList(new Integer[] { 907, 908, 921, 923, 712 });
	}

	public interface BANK_STATUS {
		int ACTIVE = 1;
		int INACTIVE = 0;
	}
	
	enum PayType {
		ONLINE(0, "ONLINE"),
		OFFLINE(1, "OFFLINE"),
		WITHDRAW(3, "WITHDRAW"),
		MOMO_DEP(4, "MOMO_DEP"),
		ZALO_DEP(5, "ZALO_DEP"),
		;
		
		private int key;
		private String value;
		
		PayType(int key, String value) {
			this.value = value;
			this.key = key;
		}
		
		public String getValue() {
			return value;
		}
		
		public int getKey() {
			return key;
		}
	}
	
	int MAINTAINCE = 99;
	int TOO_MANY_REQUEST = 20;
	int SUCCESS = 0;

}
