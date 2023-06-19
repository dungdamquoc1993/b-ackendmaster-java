package com.vinplay.interfaces.sexygame;
import org.apache.log4j.Logger;

import com.vinplay.dto.sg.CheckTransferRespDto;
import com.vinplay.dto.sg.DepositReqDto;
import com.vinplay.dto.sg.SGFundTransferRespDto;
import com.vinplay.dto.sg.WithdrawReqDto;
import com.vinplay.item.SGUserItem;
import com.vinplay.logic.CommonMethod;
import com.vinplay.logic.InitData;
import com.vinplay.usercore.utils.GameThirdPartyInit;

public class DepositWithDrawSGService extends BaseSGService {
	private static final Logger logger = Logger.getLogger(DepositWithDrawSGService.class);

	public SGFundTransferRespDto depositTransfer(Double amount, String loginname, String ip) throws Exception {
		SGFundTransferRespDto resDto = new SGFundTransferRespDto();
		try {
			DepositReqDto req = new DepositReqDto();
			MemberSGService service = new MemberSGService();
			String txCode = "SGDP" + CommonMethod.GetCurDate("yyMMddHHmmss") + InitData.getids();
			SGUserItem useritem = service.mappingUserSG(loginname);
			if(useritem == null) {
				resDto.setStatus("1");
				resDto.setDesc("User not found!");
				return resDto;
			}
			boolean res = FundTransferSGService.updateBalance(loginname, amount, 1, ip, txCode, 0);
			if (res) {
				// Set DepositPlayerMoneyResponse info
				logger.info("SG DepositTransfer playerName : " + loginname + " : " + amount
						+ " update balance success ");
				req.setTransferAmount(amount/GameThirdPartyInit.SG_RATE);
				req.setUserId(useritem.getSgid());
				req.setTxCode(txCode);

				String data = callAPI("deposit", req);
				resDto = gson.fromJson(data, SGFundTransferRespDto.class);
				if ("9999".equals(resDto.getStatus()) || "1010".equals(resDto.getStatus())) {
					logger.error("SG DepositTransfer playerName : " + loginname + " : " + amount
							+ " SG response error message " + resDto.getDesc());
				} else if ("0000".equals(resDto.getStatus())) {
					logger.info("SG DepositTransfer playerName : " + loginname + " : " + amount + " code: "
							+ resDto.getTxCode() + " success");
				} else {
					String dataCheck = callAPI("checkTransferOperation", req);
					CheckTransferRespDto resp = gson.fromJson(dataCheck, CheckTransferRespDto.class);
					if (resp.getTxStatus()==0) {
						logger.error("SG DepositTransfer playerName : " + loginname + " : " + amount
								+ " SG response error message " + resDto.getDesc());
					}else {
						logger.info("SG DepositTransfer playerName : " + loginname + " : " + amount + " code: "
							+ resDto.getTxCode() + " success");
					}
					
				}
				return resDto;
				
			} else {
				logger.info("SG DepositTransfer playerName : " + loginname + " : " + amount
						+ " update balance fail! ");
				resDto.setStatus("1");
				resDto.setDesc("SG DepositTransfer playerName : " + loginname + " : " + amount
						+ " update balance fail! ");
				return resDto;
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			logger.error("SG DepositTransfer playerName :" + loginname, ex);
			resDto.setStatus("1");
			resDto.setDesc(ex.toString());
			return resDto;
		}
	}

	public SGFundTransferRespDto withdrawTransfer(Double amount, String loginName, String ip) throws Exception {
		SGFundTransferRespDto resDto = new SGFundTransferRespDto();
		try {
			MemberSGService service = new MemberSGService();
			WithdrawReqDto reqDto = new WithdrawReqDto();
			SGUserItem useritem = service.mappingUserSG(loginName);
			String txCode = "SGWD" + CommonMethod.GetCurDate("yyMMddHHmmss") + InitData.getids();
			reqDto.setTxCode(txCode);
			reqDto.setTransferAmount(amount/GameThirdPartyInit.SG_RATE);
			reqDto.setUserId(useritem.getSgid());
			reqDto.setWithdrawType(0);
			String data = callAPI("withdraw", reqDto);
			resDto = gson.fromJson(data, SGFundTransferRespDto.class);
			if (resDto.getStatus().equals("0000")) {
					logger.info("SG WithdrawTransfer playerName : " + loginName + " : " + amount + " "
							+ resDto.getTxCode() + "success");
				boolean res = FundTransferSGService.updateBalance(loginName, amount, 0, ip, txCode, 0);
				if (res) {
					logger.info("SG WithdrawTransfer playerName : " + loginName + " : " + amount
							+ " update balance success ");
					resDto.setDesc("SG DepositTransfer playerName : " + loginName + " : " + amount
							+ " update balance success ");
				} else {
					logger.info("SG WithdrawTransfer playerName : " + loginName + " : " + amount
							+ " update balance fail! ");
					resDto.setDesc("SG WithdrawTransfer playerName : " + loginName + " : " + amount
							+ " update balance fail! ");
				}
				return resDto;
			} else if (resDto.getStatus().equals("1016")||resDto.getStatus().equals("1018")) {
					logger.info("SG WithdrawTransfer playerName : " + loginName + " SG response error message "
							+ resDto.getDesc());
			} else {
				String dataCheck = callAPI("checkTransferOperation", reqDto);
				CheckTransferRespDto resp = gson.fromJson(dataCheck, CheckTransferRespDto.class);
				if (resp.getTxStatus()==0) {
					logger.error("SG WithdrawTransfer playerName : " + loginName + " : " + amount
							+ " SG response error message " + resDto.getDesc());
				}else {
					logger.info("SG WithdrawTransfer playerName : " + loginName + " : " + amount + " code: "
						+ resDto.getTxCode() + " success");
					boolean res = FundTransferSGService.updateBalance(loginName, amount, 0, ip, txCode, 0);
					if (res) {
						logger.info("SG WithdrawTransfer playerName : " + loginName + " : " + amount
								+ " update balance success ");
						resDto.setDesc("SG WithdrawTransfer playerName : " + loginName + " : " + amount
								+ " update balance success ");
					} else {
						logger.info("SG WithdrawTransfer playerName : " + loginName + " : " + amount
								+ " update balance fail! ");
						resDto.setDesc("SG WithdrawTransfer playerName : " + loginName + " : " + amount
								+ " update balance fail! ");
					}
				}
			}
			return resDto;
		} catch (Exception ex) {
			ex.printStackTrace();
				logger.error("SG WithdrawTransfer playerName :" + loginName, ex);
			return null;
		}
	}

}
