package com.vinplay.api.backend.processors.report;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vinplay.api.backend.response.ReportMoneySystemResponse;
import com.vinplay.dal.dao.AgentDAO;
import com.vinplay.dal.dao.ReportDAO;
import com.vinplay.dal.dao.impl.AgentDAOImpl;
import com.vinplay.dal.dao.impl.ReportDaoImpl;
import com.vinplay.dal.entities.report.*;
import com.vinplay.dal.service.LogMoneyUserService;
import com.vinplay.dal.service.impl.AgentServiceImpl;
import com.vinplay.dal.service.impl.LogMoneyUserServiceImpl;
import com.vinplay.usercore.utils.GameCommon;
import com.vinplay.vbee.common.cp.BaseProcessor;
import com.vinplay.vbee.common.cp.Param;
import com.vinplay.vbee.common.response.AgentResponse;
import com.vinplay.vbee.common.response.LogUserMoneyResponse;
import com.vinplay.vbee.common.statics.Consts;
import com.vinplay.vbee.common.utils.VinPlayUtils;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.IntStream;

public class ReportMoneySystemNewProcessor
implements BaseProcessor<HttpServletRequest, String> {
    private final ArrayList<String> listAgency = getListAgent();
    public String execute(Param<HttpServletRequest> param) {
        ReportMoneySystemResponse  res = new ReportMoneySystemResponse(false, "1001");
        HttpServletRequest request = (HttpServletRequest)param.get();
        String startTime = request.getParameter("startTime");
        String endTime = request.getParameter("endTime");
        String nickName = request.getParameter("nickname");
        String referral_code = request.getParameter("code");
       
        List<ReportMoneySystemModelNew> listReportSlotMiniGame = new ArrayList<ReportMoneySystemModelNew>();
        List<ReportMoneySystemModelNew> listReportGameBai = new ArrayList<ReportMoneySystemModelNew>();
        List<ReportMoneySystemModelNew> listReportGameKhac = new ArrayList<ReportMoneySystemModelNew>();
        List<MoneyInOut> listUserIn = new ArrayList<MoneyInOut>();
        List<MoneyInOut> listUserInEvent = new ArrayList<MoneyInOut>();
        List<MoneyInOut> listUserOut = new ArrayList<MoneyInOut>();
        List<MoneyInOut> listOther = new ArrayList<MoneyInOut>();
        MoneyInOut moneyAgentIn = new MoneyInOut();
        MoneyInOut moneyAgentOut = new MoneyInOut();
        boolean endToday = true;

        try{
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            Date st = format.parse(startTime);
            Date et = format.parse(endTime);
            Date currentDate = VinPlayUtils.getCurrentDates();
            if(et.getTime() < currentDate.getTime())
                endToday = false;

            // search report money user
            HashMap<String, Long> user = new HashMap<String, Long>();
            ReportDAO dao = new ReportDaoImpl();
            HashMap<String, Long> vinOutAgent = new HashMap<String, Long>();
            ReportTotalMoneyModel totalModelStart = dao.getReportTotalMoneyAtTime(startTime, true);
            ReportTotalMoneyModel totalModelEnd = new ReportTotalMoneyModel();
            totalModelEnd = endToday ? dao.getTotalMoney(GameCommon.getValueStr("SUPER_AGENT")) : dao.getReportTotalMoneyAtTime(endTime, false);
            vinOutAgent.put("agentStart", totalModelStart.moneyAgent1 + totalModelStart.moneyAgent2 + totalModelStart.moneySuperAgent);
            vinOutAgent.put("agentEnd", totalModelEnd.moneyAgent1 + totalModelEnd.moneyAgent2 + totalModelEnd.moneySuperAgent);

            user.put("userStart", totalModelStart.moneyUser);
            user.put("userEnd", totalModelEnd.moneyUser);


            LogMoneyUserService service = new LogMoneyUserServiceImpl();
            //search all log with time
            List<LogUserMoneyResponse> list = service.searchLogMoneyUser(nickName, referral_code, "","", startTime, endTime, -1 ,-1);
            if(list == null || list.size() == 0)
                return res.toJson();
            //
            for(LogUserMoneyResponse log : list){
            	if(log.serviceName.equals("190"))
            		log.actionName = Consts.TAI_XIU_ST;
            	
            	if(log.actionName.equals(Consts.FISH.toLowerCase()))
            		log.actionName = Consts.FISH.toUpperCase();
            	
                if(Consts.GAMES_KHAC.contains(log.actionName)){
                    listReportGameKhac = processListGame(listReportGameKhac, log);
                }else if(Consts.GAMES_BAI.contains(log.actionName)){
                    listReportGameBai = processListGame(listReportGameBai, log);
                }else if(Consts.GAMES_SLOT.contains(log.actionName)){
                    listReportSlotMiniGame = processListGame(listReportSlotMiniGame, log);
                }else if(Consts.VIN_IN_USER.contains(log.actionName)){
                    listUserIn = processListMoney(listUserIn, log);
                }else if(Consts.VIN_IN_EVENT.contains(log.actionName)){
                    listUserInEvent = processListMoney(listUserInEvent, log);
                }else if(Consts.VIN_OUT_USER.contains(log.actionName)){
                    listUserOut = processListMoney(listUserOut, log);
                }else if(Consts.VIN_OTHER.contains(log.actionName)){
                    listOther = processListMoney(listOther, log);
                } 
                
//                else if(Consts.TRANSFER_MONEY.equals(log.actionName)){
//                    if(log.moneyExchange < 0){
//                        moneyAgentIn = processMoneyInToAgency(moneyAgentIn, log);
//                    }else{
//                        moneyAgentOut = processMoneyOutAgency(moneyAgentOut, log);
//                    }
//                }
            }
            
          //For games 3rd
            AgentDAO daoAgent = new AgentDAOImpl();
            List<String> fields = Arrays.asList(
            		"ifnull(sum(ifnull(wm,0)),0) wm", "ifnull(sum(ifnull(wm_win,0)),0) wm_win", 
            		"ifnull(sum(ifnull(ag,0)),0) ag", "ifnull(sum(ifnull(ag_win,0)),0) ag_win",
            		"ifnull(sum(ifnull(ibc,0)),0) ibc", "ifnull(sum(ifnull(ibc_win,0)),0) ibc_win",
            		"ifnull(sum(ifnull(cmd,0)),0) cmd", "ifnull(sum(ifnull(cmd_win,0)),0) cmd_win",
            		"ifnull(sum(ifnull(ebet,0)),0) ebet", "ifnull(sum(ifnull(ebet_win,0)),0) ebet_win",
            		"ifnull(sum(ifnull(sbo,0)),0) sbo", "ifnull(sum(ifnull(sbo_win,0)),0) sbo_win");
            List<Map<String, Object>> data = new ArrayList<>();
            data = daoAgent.ExecuteByFields(fields, nickName, startTime, endTime, referral_code, 1,10);
            if (data != null || data.size() > 0) {
            	ReportMoneySystemModelNew model = new ReportMoneySystemModelNew();
            	model.actionName = "wm";
            	model.moneyLost = Long.parseLong(data.get(0).get("wm").toString());
            	model.moneyWin = Long.parseLong(data.get(0).get("wm_win").toString());
            	model.revenuePlayGame = model.moneyWin - model.moneyLost;
            	model.revenue = model.revenuePlayGame;
            	listReportSlotMiniGame.add(model);
            	model = new ReportMoneySystemModelNew();
            	model.actionName = "ag";
            	model.moneyLost = Long.parseLong(data.get(0).get("ag").toString());
            	model.moneyWin = Long.parseLong(data.get(0).get("ag_win").toString());
            	model.revenuePlayGame = model.moneyWin - model.moneyLost;
            	model.revenue = model.revenuePlayGame;
            	listReportSlotMiniGame.add(model);
            	model = new ReportMoneySystemModelNew();
            	model.actionName = "ibc";
            	model.moneyLost = Long.parseLong(data.get(0).get("ibc").toString());
            	model.moneyWin = Long.parseLong(data.get(0).get("ibc_win").toString());
            	model.revenuePlayGame = model.moneyWin - model.moneyLost;
            	model.revenue = model.revenuePlayGame;
            	listReportSlotMiniGame.add(model);
            	model = new ReportMoneySystemModelNew();
            	model.actionName = "cmd";
            	model.moneyLost = Long.parseLong(data.get(0).get("cmd").toString());
            	model.moneyWin = Long.parseLong(data.get(0).get("cmd_win").toString());
            	model.revenuePlayGame = model.moneyWin - model.moneyLost;
            	model.revenue = model.revenuePlayGame;
            	listReportSlotMiniGame.add(model);
            	model = new ReportMoneySystemModelNew();
            	model.actionName = "ebet";
            	model.moneyLost = Long.parseLong(data.get(0).get("ebet").toString());
            	model.moneyWin = Long.parseLong(data.get(0).get("ebet_win").toString());
            	model.revenuePlayGame = model.moneyWin - model.moneyLost;
            	model.revenue = model.revenuePlayGame;
            	listReportSlotMiniGame.add(model);
            	model = new ReportMoneySystemModelNew();
            	model.actionName = "sbo";
            	model.moneyLost = Long.parseLong(data.get(0).get("sbo").toString());
            	model.moneyWin = Long.parseLong(data.get(0).get("sbo_win").toString());
            	model.revenuePlayGame = model.moneyWin - model.moneyLost;
            	model.revenue = model.revenuePlayGame;
            	listReportSlotMiniGame.add(model);
            }
            
            try {

//            	List<ReportMoneySystemModelNew> listReportGameKhacFinal = listReportGameKhac;
//            	for (String actionName : Consts.VIN_IN_EVENT) {
//            		int index = IntStream.range(0, listReportGameKhacFinal.size())
//    		    			.filter(i -> listReportGameKhacFinal.get(i).actionName.equals(actionName))
//    		    			.findFirst()
//    		    			.orElse(-1);
//            		if(index == -1) {
//            			ReportMoneySystemModelNew reportMoneySystemModelNew = new ReportMoneySystemModelNew();
//            			reportMoneySystemModelNew.actionName = actionName;
//            			reportMoneySystemModelNew.moneyLost = 0;
//            			reportMoneySystemModelNew.moneyWin = 0;
//            			reportMoneySystemModelNew.moneyOther = 0;
//            			reportMoneySystemModelNew.fee = 0;
//            			reportMoneySystemModelNew.revenuePlayGame = 0;
//            			reportMoneySystemModelNew.revenue = 0;
//            			listReportGameKhac.add(reportMoneySystemModelNew);
//        		    }
//				}

            	List<MoneyInOut> listUserInFinal = listUserIn;
            	for (String actionName : Consts.VIN_IN_USER) {
            		int index = IntStream.range(0, listUserInFinal.size())
    		    			.filter(i -> listUserInFinal.get(i).actionName.equals(actionName))
    		    			.findFirst()
    		    			.orElse(-1);
            		if(index == -1) {
            			MoneyInOut moneyInOut = new MoneyInOut();
            			moneyInOut.actionName = actionName;
            			moneyInOut.total = 0;
            			moneyInOut.fee = 0;
	                    listUserIn.add(moneyInOut);
        		    }
				}

            	List<MoneyInOut> listUserOutFinal = listUserOut;
            	for (String actionName : Consts.VIN_OUT_USER) {
            		int index = IntStream.range(0, listUserOutFinal.size())
    		    			.filter(i -> listUserOutFinal.get(i).actionName.equals(actionName))
    		    			.findFirst()
    		    			.orElse(-1);
            		if(index == -1) {
            			MoneyInOut moneyInOut = new MoneyInOut();
            			moneyInOut.actionName = actionName;
            			moneyInOut.total = 0;
            			moneyInOut.fee = 0;
            			listUserOut.add(moneyInOut);
        		    }
				}

            	List<MoneyInOut> listUserInEventFinal = listUserInEvent;
            	for (String actionName : Consts.VIN_IN_EVENT) {
            		int index = IntStream.range(0, listUserInEventFinal.size())
    		    			.filter(i -> listUserInEventFinal.get(i).actionName.equals(actionName))
    		    			.findFirst()
    		    			.orElse(-1);
            		if(index == -1) {
            			MoneyInOut moneyInOut = new MoneyInOut();
            			moneyInOut.actionName = actionName;
            			moneyInOut.total = 0;
            			moneyInOut.fee = 0;
            			listUserInEvent.add(moneyInOut);
        		    }
				}

            	List<MoneyInOut> listOtherFinal = listOther;
            	for (String actionName : Consts.VIN_IN_EVENT) {
            		int index = IntStream.range(0, listOtherFinal.size())
    		    			.filter(i -> listOtherFinal.get(i).actionName.equals(actionName))
    		    			.findFirst()
    		    			.orElse(-1);
            		if(index == -1) {
            			MoneyInOut moneyInOut = new MoneyInOut();
            			moneyInOut.actionName = actionName;
            			moneyInOut.total = 0;
            			moneyInOut.fee = 0;
            			listOther.add(moneyInOut);
        		    }
				}


                ReportMoneyModel reportMoneyModel = new ReportMoneyModel(listReportSlotMiniGame, listReportGameBai, listReportGameKhac,
                        listUserIn, listUserInEvent, listUserOut, listOther);
                reportMoneyModel.UserMoney = user;
                if (referral_code!=null && !referral_code.isEmpty()){
                    reportMoneyModel.UserMoney = null;
                }
               // reportMoneyModel.AgentMoney = vinOutAgent;

                reportMoneyModel.AgentMoneyIn = moneyAgentIn;
                reportMoneyModel.AgentMoneyOut = moneyAgentOut;
                ObjectMapper mapper = new ObjectMapper();
                return mapper.writeValueAsString((Object)reportMoneyModel);
            }
            catch (Exception e) {
                e.printStackTrace();
                return "{\"success\":false,\"errorCode\":\"1001\"}";
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return res.toJson();
    }
    private List<MoneyInOut> processListMoney(List<MoneyInOut> listReport, LogUserMoneyResponse log ){
        try{
            MoneyInOut model = new MoneyInOut();
            if(isExistMoney(listReport, log.actionName)){
                model = getElementByActionMoney(listReport, log.actionName);
            }else{
                model.actionName = log.actionName;
            }
            model.total += log.moneyExchange;
            model.fee += log.fee;
            int index = getElementIndexMoney(listReport, model.actionName);
            if(index == -1)
                listReport.add(model);
            else
                listReport.set(index, model);
            return listReport;
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }
    private List<ReportMoneySystemModelNew> processListGame(List<ReportMoneySystemModelNew> listReport, LogUserMoneyResponse log){
        try{
            ReportMoneySystemModelNew model = new ReportMoneySystemModelNew();
            if(isExist(listReport, log.actionName)){
                model = getElementByAction(listReport, log.actionName);
            }else{
                model.actionName = log.actionName;
            }
            if(log.actionName.equals(Consts.TAI_XIU)){
                if(log.moneyExchange < 0){
                    model.moneyLost += Math.abs(log.moneyExchange);
                }
                else if(log.serviceName.contains("Hoàn trả") || log.serviceName.contains("HOAN_TRA") || log.serviceName.contains("186")){
                    model.moneyOther += log.moneyExchange;
                    model.moneyLost += Math.abs(log.moneyExchange);
                } else{
                    model.moneyWin += Math.abs(log.moneyExchange);
                }
            }else if(log.actionName.equals(Consts.TAI_XIU_ST)){
                if(log.moneyExchange < 0){
                    model.moneyLost += Math.abs(log.moneyExchange);
                }
                else if(log.serviceName.contains("Hoàn trả") || log.serviceName.contains("HOAN_TRA") || log.serviceName.contains("186")){
                    model.moneyOther += log.moneyExchange;
                    model.moneyLost += Math.abs(log.moneyExchange);
                } else{
                    model.moneyWin += Math.abs(log.moneyExchange);
                }
            }
            else{
                if(log.moneyExchange < 0){
                    model.moneyLost += Math.abs(log.moneyExchange);
                }
                else{
                    model.moneyWin += Math.abs(log.moneyExchange);
                }
            }
            model.fee += log.fee;
            model.revenuePlayGame -= log.moneyExchange;
//            model.revenue += (log.moneyExchange - log.fee);
            model.revenue -= log.moneyExchange;

//            long swich = model.moneyLost;
//            model.moneyLost = model.moneyWin;
//            model.moneyWin = swich;

            int index = getElementIndex(listReport, model.actionName);
            if(index == -1)
                listReport.add(model);
            else
                listReport.set(index, model);
            
            return listReport;
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }
    // ReportMoneySystemModelNew
    private boolean isExist(List<ReportMoneySystemModelNew> listReport, String actionName){
        if(actionName == null)
            return false;
        for (ReportMoneySystemModelNew e : listReport){
            if(e.actionName.equals(actionName)){
                return true;
            }
        }
        return false;
    }
    private ReportMoneySystemModelNew getElementByAction(List<ReportMoneySystemModelNew> listReport, String actionName){
        for (ReportMoneySystemModelNew e : listReport){
            if(e.actionName.equals(actionName)){
                return e;
            }
        }
        return null;
    }
    private int getElementIndex(List<ReportMoneySystemModelNew> listReport, String actionName){
        int i = 0;
        for (ReportMoneySystemModelNew e : listReport){
            if(e.actionName.equals(actionName)){
                return i;
            }else{
                i ++;
            }
        }
        return -1;
    }
    // end ReportMoneySystemModelNew
    // Money In out
    private boolean isExistMoney(List<MoneyInOut> listReport, String actionName){
        if(actionName == null)
            return false;
        for (MoneyInOut e : listReport){
            if(e.actionName.equals(actionName)){
                return true;
            }
        }
        return false;
    }
    private MoneyInOut getElementByActionMoney(List<MoneyInOut> listReport, String actionName){
        for (MoneyInOut e : listReport){
            if(e.actionName.equals(actionName)){
                return e;
            }
        }
        return null;
    }
    private int getElementIndexMoney(List<MoneyInOut> listReport, String actionName){
        int i = 0;
        for (MoneyInOut e : listReport){
            if(e.actionName.equals(actionName)){
                return i;
            }else{
                i ++;
            }
        }
        return -1;
    }
    // end ReportMoneySystemModelNew

    // process money agency ( from user to agency)
    private MoneyInOut processMoneyInToAgency(MoneyInOut agencyMoneyIn, LogUserMoneyResponse log){
        try{
            if(log.moneyExchange > 0)
                return agencyMoneyIn;

            if(!isInListAgency(log.description))
                return agencyMoneyIn;
            agencyMoneyIn.total += log.moneyExchange;
            agencyMoneyIn.fee += log.fee;
            return agencyMoneyIn;
        }catch (Exception e){
            return null;
        }
    }

    // from agency to user
    private MoneyInOut processMoneyOutAgency(MoneyInOut agencyMoneyOut, LogUserMoneyResponse log){
        try{
            if(log.moneyExchange < 0)
                return agencyMoneyOut;

            if(!isInListAgency(log.description))
                return agencyMoneyOut;
            agencyMoneyOut.total += log.moneyExchange;
            agencyMoneyOut.fee += log.fee;
            return agencyMoneyOut;
        }catch (Exception e){
            return null;
        }
    }

    // getListAgent

    private ArrayList<String> getListAgent(){
        try{
            AgentServiceImpl service = new AgentServiceImpl();
            List<AgentResponse> agents = service.listAgent();
            ArrayList<String> agentNames = new ArrayList<String>();
            if (agents != null && agents.size() > 0) {
                for (AgentResponse agent : agents) {
                    agentNames.add(agent.nickName);
                }
            }
            return agentNames;
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }

    }
    private boolean isInListAgency(String content){
        if(listAgency == null)
            return false;

        for (String agency: listAgency){
            if(content.contains(agency))
                return true;
        }
        return false;
    }

}

