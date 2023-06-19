package com.vinplay.pay.server;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.vinplay.dichvuthe.response.RechargePaywellResponse;
import com.vinplay.payment.dao.RechargePaygateDao;
import com.vinplay.payment.dao.WithDrawPaygateDao;
import com.vinplay.payment.dao.impl.RechargePaygateDaoImpl;
import com.vinplay.payment.dao.impl.WithDrawPaygateDaoImpl;
import com.vinplay.payment.entities.WithDrawPaygateModel;
import com.vinplay.payment.service.WithDrawOneClickPayService;
import com.vinplay.payment.service.impl.WithDrawOneClickPayServiceImpl;
import com.vinplay.payment.utils.PayCommon;

public class TaskAutoPayment extends java.util.TimerTask {
	private static final Logger LOGGER = Logger.getLogger(TaskAutoPayment.class);
	@Override
	public void run() {
		LOGGER.info("Start Job Update bank status automation , time=" + LocalDateTime.now());
		
//		PaymentConfigService configService = new PaymentConfigServiceImpl();
//		PaymentConfig configObj = configService.getConfigByKey(PaymentConstant.PROVIDER.CLICK_PAY);
//		
//		//checkstatus for both
//		if (configObj.getConfig().getStatus() == PaymentConstant.BANK_STATUS.INACTIVE) {
//			return;
//		}
		
		//job deposit
		RechargePaygateDao rechard = new RechargePaygateDaoImpl();
		try {
			rechard.updatePendingStatusToFailedAfterMinus(60, "all");
		} catch (Exception e) {
			LOGGER.error(e);
		}
		
//		//job withdraw
//		WithDrawOneClickPayService withdrawOneClickPayService = new WithDrawOneClickPayServiceImpl();
//		WithDrawPaygateDao withDraw = new WithDrawPaygateDaoImpl();
//		try {
//			//call db to get withdraw request in 7 minutes , in RECEIVED status
//			List<WithDrawPaygateModel> lstRecords = new ArrayList<WithDrawPaygateModel>();
//			lstRecords = withDraw.GetRecevied(10);
//			if (lstRecords == null ||lstRecords.isEmpty())
//				return;
//			//check these order in 3rd
//			for (WithDrawPaygateModel wd : lstRecords) {
//				RechargePaywellResponse res = withdrawOneClickPayService.checkStatus(wd.CartId);
//				if (res.getCode() == 0) {//status in 3rd
//					//update in db to success 
//					withdrawOneClickPayService.notify(wd, PayCommon.PAYSTATUS.SUCCESS.getId());
//				} else {
//					withdrawOneClickPayService.notify(wd, PayCommon.PAYSTATUS.FAILED.getId());
//				}
//			}
//		} catch (Exception e) {
//			LOGGER.error(e);
//		}
	}

}
