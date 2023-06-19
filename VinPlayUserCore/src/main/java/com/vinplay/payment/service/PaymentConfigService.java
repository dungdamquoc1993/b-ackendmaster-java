package com.vinplay.payment.service;

import java.util.List;
import com.vinplay.payment.entities.PaymentConfig;
import com.vinplay.payment.entities.Response;

public interface PaymentConfigService {
//	public Response add(List<PaymentConfig> paymentConfig);

//	public Response addProvider(PaymentConfig paymentConfig);
//
//	public Response update(List<PaymentConfig> paymentConfig);
//
//	public Response updateProvider(PaymentConfig paymentConfig);
//
//	public Response deleteProvider(String key);

	public List<PaymentConfig> getConfig();

	public PaymentConfig getConfigByKey(String key);

	public Response getConfig(String key);

	public Response getBanks(String key);

	public Response getBankWithdraw(String bankName, Integer isWithdraw);
}
