package com.vinplay.payment.entities;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;

public class WithDrawPrinceReponse {
    
    public WithDrawPrinceReponse(String orderid, String transactionid, long amount, long real_amount, String custome,
			int status, String message) {
		super();
		this.orderid = orderid;
		this.transactionid = transactionid;
		this.amount = amount;
		this.real_amount = real_amount;
		this.custome = custome;
		this.status = status;
		this.message = message;
	}
	
    public WithDrawPrinceReponse() {
		super();
	}

    public String transactionid;
	public long amount;
    public long real_amount;
    public String custome;
    public int status;
    public String message;
    public String orderid;
    
    public String toJson() {
		ObjectWriter ow = new ObjectMapper().writer();ow.with(SerializationFeature.INDENT_OUTPUT);
		try {
			String json = ow.writeValueAsString(this);
			return json;
		} catch (Exception e) {
			return null;
		}
	}
}
