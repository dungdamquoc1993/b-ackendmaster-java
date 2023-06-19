package com.vinplay.api.backend.processors.report;

import com.vinplay.api.backend.response.ReportLogUserResponse;
import com.vinplay.dal.dao.AgentDAO;
import com.vinplay.dal.dao.impl.AgentDAOImpl;
import com.vinplay.payment.utils.Constant;
import com.vinplay.vbee.common.cp.BaseProcessor;
import com.vinplay.vbee.common.cp.Param;
import com.vinplay.vbee.common.response.BaseResponse;
import org.apache.log4j.Logger;
import javax.servlet.http.HttpServletRequest;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SearchAgencyProfitProcessor
        implements BaseProcessor<HttpServletRequest, String> {
    private static final Logger logger = Logger.getLogger((String)"api_agent");

	public String execute(Param<HttpServletRequest> param) {
		ReportLogUserResponse res = new ReportLogUserResponse(false, "1001");
		HttpServletRequest request = (HttpServletRequest) param.get();
		String serPath = request.getServletPath();
		if (serPath == null || serPath.trim().isEmpty() || serPath != "/api_backend") {
			return BaseResponse.error(Constant.ERROR_PARAM, "Not allow access this api");
		}

		String monthStr = request.getParameter("m");
		String yearStr = request.getParameter("y");
		String referral_code = request.getParameter("code");
		String pageNumberStr = request.getParameter("pg");
		String limitStr = request.getParameter("mi");
		int pageNumber = 0;
		int limit = 0;
		try {
			int month = Integer.parseInt(monthStr);
			int year = Integer.parseInt(yearStr);
		} catch (NumberFormatException e) {
			return BaseResponse.error(Constant.ERROR_PARAM, "month or year format");
		}

		try {
			pageNumber = Integer.parseInt(pageNumberStr);
			limit = Integer.parseInt(limitStr);
		} catch (NumberFormatException e) {
			return BaseResponse.error(Constant.ERROR_PARAM, "pageNumber or limit format");
		}

		if (pageNumber <= 0)
			pageNumber = 1;

		if (limit < 0)
			limit = 15;

		List<Map<String, Object>> detailAgencies = new ArrayList<>();
		List<Map<String, Object>> agencies = new ArrayList<>();
		System.out.println("Start list: " + getCurrentTime("yyyy-MM-dd HH:mm:sss"));
		try {
			AgentDAO dao = new AgentDAOImpl();
			agencies = dao.getAgenciesLevel1(referral_code, pageNumber, limit);
			if (agencies == null || agencies.size() == 0)
				return res.toJson();

			for (int i = 0; i < agencies.size() - 1; i++) {
				List<Map<String, Object>> agencyMembers = new ArrayList<>();
				String nickname = agencies.get(i).get("nickname").toString();
				String nameAgent = agencies.get(i).get("nameagent").toString();
				String agencyCode = agencies.get(i).get("code").toString();
				String codeChilds = dao.getListCodeChildsByCode(agencyCode);
				String agencyCodes = agencyCode + (codeChilds == null || codeChilds.isEmpty() ? "" : "," + codeChilds);
				String createTime = agencies.get(i).get("createtime").toString();
				agencyMembers = dao.getMemberPassed4Agency(agencyCodes, yearStr + "-" + monthStr);
				int totalMemberActives = agencyMembers == null ? 0 : agencyMembers.size(); 
				// TODO: Get level of agency
				// level of agency: 1->Cu | 2->Ag | 3->Au | 4->Diamond
				int levelAgency = 0;
				double ratiosport = 0;
				double ratiocasino = 0;
				double ratioegame = 0;
				if(totalMemberActives < 5) {
					levelAgency = 0;
					ratiosport = 0;
					ratiocasino = 0;
					ratioegame = 0;
				}
				
				if (totalMemberActives >= 5 && totalMemberActives < 10) {
					levelAgency = 1;
					ratiosport = 0.2;
					ratiocasino = 0.1;
					ratioegame = 3;
				}

				if (totalMemberActives >= 10 && totalMemberActives < 15) {
					levelAgency = 2;
					ratiosport = 0.25;
					ratiocasino = 0.15;
					ratioegame = 4;
				}

				if (totalMemberActives >= 15 && totalMemberActives < 20) {
					levelAgency = 3;
					ratiosport = 0.3;
					ratiocasino = 0.2;
					ratioegame = 5;
				}

				if (totalMemberActives >= 20 && totalMemberActives < 25) {
					levelAgency = 4;
					ratiosport = 0.4;
					ratiocasino = 0.3;
					ratioegame = 7;
				}

				Map<String, Object> hashmap = new HashMap<>();
				List<Map<String, Object>> currentDetails = new ArrayList<>();
				currentDetails = dao.getProfitComponents4Agency(agencyCode, yearStr + "-" + monthStr);
				if(currentDetails == null || currentDetails.size() != 1) {
					hashmap.put("nickname", nickname);
					hashmap.put("nameAgent", nameAgent);
					hashmap.put("createTime", createTime);
					hashmap.put("code", agencyCode);
					hashmap.put("totalBet", String.format("%,.2f", 0));
					hashmap.put("totalWin", String.format("%,.2f", 0));
					hashmap.put("totalBonus", String.format("%,.2f", 0));
					hashmap.put("totalFund", String.format("%,.2f", 0));
					hashmap.put("feeGameThrd", String.format("%,.2f", 0));
					hashmap.put("feeGameThrd", String.format("%,.2f", 0));
					hashmap.put("totalDeposit", String.format("%,.2f", 0));
					hashmap.put("totalWithdraw", String.format("%,.2f", 0));
					hashmap.put("profit", String.format("%,.2f", 0));
					hashmap.put("commission", String.format("%,.2f", 0));
					hashmap.put("memberActives", String.valueOf(totalMemberActives));
					detailAgencies.add(hashmap);
				} else {
					hashmap.put("code", agencyCode);
					double totalbet = 0;
					double totalwin = 0;
					double totalbonus = 0;
					double totalsportbonus = 0;
					double totalcasinobonus = 0;
					double totalegamebonus = 0;
					double totalfund = 0;
					double totalag = 0;
					double totalcmd = 0;
					double totalwm = 0;
					double totalibc = 0;
					double totalegame = 0;
					double feeGamethrd = 0;
					double totalDeposit = 0;
					double totalWithdraw = 0;
					double profit = 0;
					totalbet = currentDetails.get(0).get("totalbet") != null
							? Double.parseDouble(currentDetails.get(0).get("totalbet").toString() + "d")
							: 0;
					totalwin = currentDetails.get(0).get("totalwin") != null
							? Double.parseDouble(currentDetails.get(0).get("totalwin").toString() + "d")
							: 0;
					totalbonus = currentDetails.get(0).get("totalbonus") != null
							? Double.parseDouble(currentDetails.get(0).get("totalbonus").toString() + "d")
							: 0;
					totalsportbonus = currentDetails.get(0).get("totalsportbonus") != null
							? Double.parseDouble(currentDetails.get(0).get("totalsportbonus").toString() + "d")
							: 0;
					totalcasinobonus = currentDetails.get(0).get("totalcasinobonus") != null
							? Double.parseDouble(currentDetails.get(0).get("totalcasinobonus").toString() + "d")
							: 0;
					totalegamebonus = currentDetails.get(0).get("totalegamebonus") != null
							? Double.parseDouble(currentDetails.get(0).get("totalegamebonus").toString() + "d")
							: 0;
					totalfund = currentDetails.get(0).get("totalfund") != null
							? Double.parseDouble(currentDetails.get(0).get("totalfund").toString() + "d")
							: 0;
					totalDeposit = currentDetails.get(0).get("totaldeposit") != null
							? Double.parseDouble(currentDetails.get(0).get("totaldeposit").toString() + "d")
							: 0;
					totalWithdraw = currentDetails.get(0).get("totalwithdraw") != null
							? Double.parseDouble(currentDetails.get(0).get("totalwithdraw").toString() + "d")
							: 0;
					totalag = currentDetails.get(0).get("totalag") != null
							? Double.parseDouble(currentDetails.get(0).get("totalag").toString() + "d")
							: 0;
					totalcmd = currentDetails.get(0).get("totalcmd") != null
							? Double.parseDouble(currentDetails.get(0).get("totalcmd").toString() + "d")
							: 0;
					totalwm = currentDetails.get(0).get("totalwm") != null
							? Double.parseDouble(currentDetails.get(0).get("totalwm").toString() + "d")
							: 0;
					totalibc = currentDetails.get(0).get("totalibc") != null
							? Double.parseDouble(currentDetails.get(0).get("totalibc").toString() + "d")
							: 0;
					totalegame = currentDetails.get(0).get("totalegame") != null
							? Double.parseDouble(currentDetails.get(0).get("totalegame").toString() + "d")
							: 0;
					feeGamethrd = totalag * 0.1 + totalwm * 0.1 + totalibc * 0.12 + totalcmd * 0.12;
					// TODO: expressions calculator profit --> old
//					profit = totalbet - totalwin - totalbonus - totalfund - feeGamethrd;
					//New expressions calculator profit
					profit = totalDeposit - totalWithdraw - totalbonus - totalfund - feeGamethrd;
					double commisSport = 0;
					double commissCasino = 0;
					double commisEgame = 0;
					double commission = 0;
	//				Cách tính cũ theo level của đại lý và theo nhóm game
	//                if (levelAgency > 0) {
	//                    commisSport = ratiosport * (totalibc + totalcmd - totalsportbonus);
	//                    commissCasino = ratiocasino * (totalwm + totalag - totalcasinobonus);
	//                    commisEgame = ratioegame * (totalegame - totalegamebonus);
	//                    commission = commisSport + commissCasino + commisEgame;
	//                } else {
	//                    commission = 0;
	//                }
	//				Cách tính mới theo doanh thu đạt được của đại ký * tỷ lệ phần trăm hoa hồng kết hợp với số member tối
	//				thiểu phải = 5
					double ratio = 0;
					if(profit >= 1000000l && profit <= 200000000l)
						ratio = 0.3;
					
					if(profit >= 201000000l && profit <= 900000000l)
						ratio = 0.35;
					
					if(profit >= 901000000l && profit <= 1500000000l)
						ratio = 0.4;
					
					if(profit >= 1501000000l && profit <= 3000000000l)
						ratio = 0.45;
					
					if(profit > 3000000000l)
						ratio = 0.5;
					
					if (levelAgency > 0) {
						commission = ratio * profit;
					} else {
						commission = 0;
					}
	
					//Theo yc của @everlyn
//					if (profit > 0) {
//						commission = 0;
//					}
	
					hashmap.put("nickname", nickname);
					hashmap.put("nameAgent", nameAgent);
					hashmap.put("createTime", createTime);
					hashmap.put("code", agencyCode);
					hashmap.put("totalBet", String.format("%,.2f", totalbet));
					hashmap.put("totalWin", String.format("%,.2f", totalwin));
					hashmap.put("totalBonus", String.format("%,.2f", totalbonus));
					hashmap.put("totalFund", String.format("%,.2f", totalfund));
					hashmap.put("feeGameThrd", String.format("%,.2f", feeGamethrd));
					hashmap.put("totalDeposit", String.format("%,.2f", totalDeposit));
					hashmap.put("totalWithdraw", String.format("%,.2f", totalWithdraw));
					hashmap.put("profit", String.format("%,.2f", profit));
					hashmap.put("commission", String.format("%,.2f", commission));
					hashmap.put("memberActives", String.valueOf(totalMemberActives));
				}
				
				detailAgencies.add(hashmap);
			}
			
			List<Map<String, Object>> data = new ArrayList<>();
			res.total = Integer.parseInt(agencies.get(agencies.size() - 1).get("total").toString());
			System.out.println("End list: " + getCurrentTime("yyyy-MM-dd HH:mm:sss"));
			System.out.println("Start sum: " + getCurrentTime("yyyy-MM-dd HH:mm:sss"));
			Map<String, Object> totalSumAgency = CalculatorSumProfit(yearStr, monthStr);
			Map<String, Object> totalSumNotAgency = CalculatorSumProfitNotAgent(yearStr, monthStr);
			System.out.println("End sum: " + getCurrentTime("yyyy-MM-dd HH:mm:sss"));
			Map<String, Object> map = new HashMap<>();
			map.put("sumaryAgency", totalSumAgency);
			data.add(map);
			map = new HashMap<>();
			map.put("sumaryNotAgency", totalSumNotAgency);
			data.add(map);
			map = new HashMap<>();
			map.put("agencies", detailAgencies);
			data.add(map);
			res.setData(data);
			res.setErrorCode("0");
			res.setSuccess(true);
			return res.toJson();
		} catch (Exception e) {
			e.printStackTrace();
			logger.error((Object) e);
			return "{\"success\":false,\"errorCode\":\"1001\"}";
		}
	}
	
	private String getCurrentTime(String pattern) {
    	if(pattern == null || pattern.trim().isEmpty())
    		pattern = "yyyyMMddHHmmssSSS";
    	
    	SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
    	return  simpleDateFormat.format(new Date());
    }
	
	private Map<String, Object> CalculatorSumProfit(String yearStr, String monthStr){
		List<Map<String, Object>> agencies = new ArrayList<>();
		try {
			AgentDAO dao = new AgentDAOImpl();
			agencies = dao.getAgenciesLevel1("", -1, -1); 
			if (agencies == null || agencies.size() == 0) {
				Map<String, Object> map = new HashMap<>();
				map.put("totalBet", 0);
				map.put("totalWin", 0);
				map.put("totalBonus", 0);
				map.put("totalFund", 0);
				map.put("totalFeeGameThrd", 0);
				map.put("toalProfit", 0);
				map.put("totalCommission", 0);
				map.put("totalDeposit", 0);
				map.put("totalWithdraw", 0);
				map.put("memberActives", 0);
				return map;
			}
				
			Double totalBet = 0d; Double totalWin = 0d;
			Double totalBonus = 0d; Double totalFund = 0d;
			Double totalFeeGameThrd = 0d; Double totalProfit = 0d;
			Double totalCommission = 0d; Double totalDeposit = 0d;
			Double totalWithdraw = 0d; Double totalMemberActives = 0d;
			for (int i = 0; i < agencies.size() - 1; i++) {
				List<Map<String, Object>> agencyMembers = new ArrayList<>();
				String agencyCode = agencies.get(i).get("code").toString();
				String codeChilds = dao.getListCodeChildsByCode(agencyCode);
				String agencyCodes = agencyCode + (codeChilds == null || codeChilds.isEmpty() ? "" : "," + codeChilds);
				agencyMembers = dao.getMemberPassed4Agency(agencyCodes, yearStr + "-" + monthStr);
				int totalMemberActiveAgent = agencyMembers == null ? 0 : agencyMembers.size();
				// TODO: Get level of agency
				// level of agency: 1->Cu | 2->Ag | 3->Au | 4->Diamond
				int levelAgency = 0;
				double ratioSport = 0;
				double ratioCasino = 0;
				double ratioEgame = 0;
				if(totalMemberActives < 5) {
					levelAgency = 0;
					ratioSport = 0;
					ratioCasino = 0;
					ratioEgame = 0;
				}
				
				if (totalMemberActiveAgent >= 5 && totalMemberActiveAgent < 10) {
					levelAgency = 1;
					ratioSport = 0.2;
					ratioCasino = 0.1;
					ratioEgame = 3;
				}

				if (totalMemberActiveAgent >= 10 && totalMemberActiveAgent < 15) {
					levelAgency = 2;
					ratioSport = 0.25;
					ratioCasino = 0.15;
					ratioEgame = 4;
				}

				if (totalMemberActiveAgent >= 15 && totalMemberActiveAgent < 20) {
					levelAgency = 3;
					ratioSport = 0.3;
					ratioCasino = 0.2;
					ratioEgame = 5;
				}

				if (totalMemberActiveAgent >= 20 && totalMemberActiveAgent < 25) {
					levelAgency = 4;
					ratioSport = 0.4;
					ratioCasino = 0.3;
					ratioEgame = 7;
				}

				Map<String, Object> hashmap = new HashMap<>();
				List<Map<String, Object>> currentDetails = new ArrayList<>();
				currentDetails = dao.getProfitComponents4Agency(agencyCode, yearStr + "-" + monthStr);
				if(currentDetails != null && currentDetails.size() == 1) {
					hashmap.put("code", agencyCode);
					double totalBetAgent = 0;
					double totalWinAgent = 0;
					double totalBonusAgent = 0;
					double totalSportBonusAgent = 0;
					double totalCasinoBonusAgent = 0;
					double totalEgameBonusAgent = 0;
					double totalFundAgent = 0;
					double totalAgAgent = 0;
					double totalCmdAgent = 0;
					double totalWmAgent = 0;
					double totalIbcAgent = 0;
					double totalEgameAgent = 0;
					double totalFeeGamethrdAgent = 0;
					double totalProfitAgent = 0;
					double totalDepositAgent = 0;
					double totalWithdrawAgent = 0;
					totalBetAgent = currentDetails.get(0).get("totalbet") != null
							? Double.parseDouble(currentDetails.get(0).get("totalbet").toString() + "d")
							: 0;
					totalWinAgent = currentDetails.get(0).get("totalwin") != null
							? Double.parseDouble(currentDetails.get(0).get("totalwin").toString() + "d")
							: 0;
					totalBonusAgent = currentDetails.get(0).get("totalbonus") != null
							? Double.parseDouble(currentDetails.get(0).get("totalbonus").toString() + "d")
							: 0;
					totalSportBonusAgent = currentDetails.get(0).get("totalsportbonus") != null
							? Double.parseDouble(currentDetails.get(0).get("totalsportbonus").toString() + "d")
							: 0;
					totalCasinoBonusAgent = currentDetails.get(0).get("totalcasinobonus") != null
							? Double.parseDouble(currentDetails.get(0).get("totalcasinobonus").toString() + "d")
							: 0;
					totalEgameBonusAgent = currentDetails.get(0).get("totalegamebonus") != null
							? Double.parseDouble(currentDetails.get(0).get("totalegamebonus").toString() + "d")
							: 0;
					totalFundAgent = currentDetails.get(0).get("totalfund") != null
							? Double.parseDouble(currentDetails.get(0).get("totalfund").toString() + "d")
							: 0;
					totalDepositAgent = currentDetails.get(0).get("totaldeposit") != null
							? Double.parseDouble(currentDetails.get(0).get("totaldeposit").toString() + "d")
							: 0;
					totalWithdrawAgent = currentDetails.get(0).get("totalwithdraw") != null
							? Double.parseDouble(currentDetails.get(0).get("totalwithdraw").toString() + "d")
							: 0;
					totalAgAgent = currentDetails.get(0).get("totalag") != null
							? Double.parseDouble(currentDetails.get(0).get("totalag").toString() + "d")
							: 0;
					totalCmdAgent = currentDetails.get(0).get("totalcmd") != null
							? Double.parseDouble(currentDetails.get(0).get("totalcmd").toString() + "d")
							: 0;
					totalWmAgent = currentDetails.get(0).get("totalwm") != null
							? Double.parseDouble(currentDetails.get(0).get("totalwm").toString() + "d")
							: 0;
					totalIbcAgent = currentDetails.get(0).get("totalibc") != null
							? Double.parseDouble(currentDetails.get(0).get("totalibc").toString() + "d")
							: 0;
					totalEgameAgent = currentDetails.get(0).get("totalegame") != null
							? Double.parseDouble(currentDetails.get(0).get("totalegame").toString() + "d")
							: 0;
					totalFeeGamethrdAgent = totalAgAgent * 0.1 + totalWmAgent * 0.1 + totalIbcAgent * 0.12 + totalCmdAgent * 0.12;
					// TODO: expressions calculator profit --> old
//					totalProfitAgent = totalBetAgent - totalWinAgent - totalBonusAgent - totalFundAgent - totalFeeGamethrdAgent;
					//New expressions calculator profit
					totalProfitAgent = totalDepositAgent - totalWithdrawAgent - totalBonusAgent - totalFundAgent - totalFeeGamethrdAgent;
					double commission = 0;
	//-------------- Cách tính cũ theo level của đại lý và theo nhóm game --------------//
//					double commisSport = 0;
//					double commissCasino = 0;
//					double commisEgame = 0;
	//                if (levelAgency > 0) {
	//                    commisSport = ratioSport * (totalIbcAgent + totalCmdAgent - totalSportBonusAgent);
	//                    commissCasino = ratioCasino * (totalWmAgent + totalAgAgent - totalCasinoBonusAgent);
	//                    commisEgame = ratioEgame * (totalEgameAgent - totalEgameBonusAgent);
	//                    commission = commisSport + commissCasino + commisEgame;
	//                } else {
	//                    commission = 0;
	//                }
					
	//-------------- Cách tính mới theo doanh thu đạt được của đại ký * tỷ lệ phần trăm hoa hồng kết hợp với số member tối-------//
	//				thiểu phải = 5
					double ratio = 0;
					if(totalProfitAgent >= 100000000l && totalProfitAgent <= 200000000l)
						ratio = 0.3;
					
					if(totalProfitAgent >= 201000000l && totalProfitAgent <= 900000000l)
						ratio = 0.35;
					
					if(totalProfitAgent >= 901000000l && totalProfitAgent <= 1500000000l)
						ratio = 0.4;
					
					if(totalProfitAgent >= 1501000000l && totalProfitAgent <= 3000000000l)
						ratio = 0.45;
					
					if(totalProfitAgent > 3000000000l)
						ratio = 0.5;
					
					if (levelAgency > 0) {
						commission = ratio * totalProfitAgent;
					} else {
						commission = 0;
					}
					
					//Theo yc của @everlyn
//					if (totalProfitAgent > 0) {
//						commission = 0;
//					}
					
					totalBet += totalBetAgent; 
					totalWin += totalWinAgent;
					totalBonus += totalBonusAgent; 
					totalFund += totalFundAgent;
					totalFeeGameThrd += totalFeeGamethrdAgent; 
					totalProfit += totalProfitAgent;
					totalCommission += commission;
					totalDeposit += totalDepositAgent;
					totalWithdraw += totalWithdrawAgent;
					totalMemberActives += totalMemberActiveAgent;
				}
			}
			
			Map<String, Object> map = new HashMap<>();
			map.put("totalBet", String.format("%,.2f", totalBet));
			map.put("totalWin", String.format("%,.2f", totalWin));
			map.put("totalBonus",String.format("%,.2f", totalBonus));
			map.put("totalFund", String.format("%,.2f", totalFund));
			map.put("totalFeeGameThrd", String.format("%,.2f", totalFeeGameThrd));
			map.put("toalProfit", String.format("%,.2f", totalProfit));
			map.put("totalCommission", String.format("%,.2f", totalCommission));
			map.put("totalDeposit", String.format("%,.2f", totalDeposit));
			map.put("totalWithdraw", String.format("%,.2f", totalWithdraw));
			map.put("memberActives", String.valueOf(totalMemberActives));
			return map;
		}catch (Exception e) {
			Map<String, Object> map = new HashMap<>();
			map.put("totalBet", 0);
			map.put("totalWin", 0);
			map.put("totalBonus", 0);
			map.put("totalFund", 0);
			map.put("totalFeeGameThrd", 0);
			map.put("toalProfit", 0);
			map.put("totalCommission", 0);
			map.put("totalDeposit", 0);
			map.put("totalWithdraw", 0);
			map.put("memberActives", 0);
			return map;
		}
	}

	private Map<String, Object> CalculatorSumProfitNotAgent(String yearStr, String monthStr){
		AgentDAO dao = new AgentDAOImpl();
		int agencyMembers = 0;
		try{
			agencyMembers = dao.getCountMemberNotAgency(yearStr + "-" + monthStr);
		}catch (Exception e) { }
		
		try {
			List<Map<String, Object>> currentDetails = new ArrayList<>();
			currentDetails = dao.getProfitComponentsNotAgency(yearStr + "-" + monthStr);
			if(currentDetails != null && currentDetails.size() == 1) {
				double totalBetAgent = 0;
				double totalWinAgent = 0;
				double totalBonusAgent = 0;
				double totalFundAgent = 0;
				double totalAgAgent = 0;
				double totalCmdAgent = 0;
				double totalWmAgent = 0;
				double totalIbcAgent = 0;
				double totalFeeGamethrdAgent = 0;
				double totalProfitAgent = 0;
				double totalDepositAgent = 0;
				double totalWithdrawAgent = 0;
				totalBetAgent = currentDetails.get(0).get("totalbet") != null
						? Double.parseDouble(currentDetails.get(0).get("totalbet").toString() + "d")
						: 0;
				totalWinAgent = currentDetails.get(0).get("totalwin") != null
						? Double.parseDouble(currentDetails.get(0).get("totalwin").toString() + "d")
						: 0;
				totalBonusAgent = currentDetails.get(0).get("totalbonus") != null
						? Double.parseDouble(currentDetails.get(0).get("totalbonus").toString() + "d")
						: 0;
				totalFundAgent = currentDetails.get(0).get("totalfund") != null
						? Double.parseDouble(currentDetails.get(0).get("totalfund").toString() + "d")
						: 0;
				totalDepositAgent = currentDetails.get(0).get("totaldeposit") != null
						? Double.parseDouble(currentDetails.get(0).get("totaldeposit").toString() + "d")
						: 0;
				totalWithdrawAgent = currentDetails.get(0).get("totalwithdraw") != null
						? Double.parseDouble(currentDetails.get(0).get("totalwithdraw").toString() + "d")
						: 0;
				totalAgAgent = currentDetails.get(0).get("totalag") != null
						? Double.parseDouble(currentDetails.get(0).get("totalag").toString() + "d")
						: 0;
				totalCmdAgent = currentDetails.get(0).get("totalcmd") != null
						? Double.parseDouble(currentDetails.get(0).get("totalcmd").toString() + "d")
						: 0;
				totalWmAgent = currentDetails.get(0).get("totalwm") != null
						? Double.parseDouble(currentDetails.get(0).get("totalwm").toString() + "d")
						: 0;
				totalIbcAgent = currentDetails.get(0).get("totalibc") != null
						? Double.parseDouble(currentDetails.get(0).get("totalibc").toString() + "d")
						: 0;
				totalFeeGamethrdAgent = totalAgAgent * 0.1 + totalWmAgent * 0.1 + totalIbcAgent * 0.12 + totalCmdAgent * 0.12;
				// TODO: expressions calculator profit
				totalProfitAgent = totalBetAgent - totalWinAgent - totalBonusAgent - totalFundAgent - totalFeeGamethrdAgent;
				Map<String, Object> map = new HashMap<>();
				map.put("totalBet", String.format("%,.2f", totalBetAgent));
				map.put("totalWin", String.format("%,.2f", totalWinAgent));
				map.put("totalBonus",String.format("%,.2f", totalBonusAgent));
				map.put("totalFund", String.format("%,.2f", totalFundAgent));
				map.put("totalFeeGameThrd", String.format("%,.2f", totalFeeGamethrdAgent));
				map.put("toalProfit", String.format("%,.2f", totalProfitAgent));
				map.put("totalCommission", String.format("%,.2f", 0.0));
				map.put("totalDeposit", String.format("%,.2f", totalDepositAgent));
				map.put("totalWithdraw", String.format("%,.2f", totalWithdrawAgent));
				map.put("memberActives", String.valueOf(agencyMembers));
				return map;
			}else {
				Map<String, Object> map = new HashMap<>();
				map.put("totalBet", 0);
				map.put("totalWin", 0);
				map.put("totalBonus", 0);
				map.put("totalFund", 0);
				map.put("totalFeeGameThrd", 0);
				map.put("totalDeposit", 0);
				map.put("totalWithdraw", 0);
				map.put("toalProfit", 0);
				map.put("totalCommission", 0);
				map.put("memberActives", String.valueOf(agencyMembers));
				return map;
			}
		}catch (Exception e) {
			Map<String, Object> map = new HashMap<>();
			map.put("totalBet", 0);
			map.put("totalWin", 0);
			map.put("totalBonus", 0);
			map.put("totalFund", 0);
			map.put("totalFeeGameThrd", 0);
			map.put("totalDeposit", 0);
			map.put("totalWithdraw", 0);
			map.put("toalProfit", 0);
			map.put("totalCommission", 0);
			map.put("memberActives", String.valueOf(agencyMembers));
			return map;
		}
	}
}