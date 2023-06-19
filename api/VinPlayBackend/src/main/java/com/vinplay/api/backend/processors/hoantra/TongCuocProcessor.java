package com.vinplay.api.backend.processors.hoantra;

import com.vinplay.api.backend.response.TongCuocResponse;
import com.vinplay.dal.dao.AgentDAO;
import com.vinplay.dal.dao.impl.AgentDAOImpl;
import com.vinplay.dal.entities.agent.UserDetailAgentModel;
import com.vinplay.vbee.common.cp.BaseProcessor;
import com.vinplay.vbee.common.cp.Param;
import com.vinplay.vbee.common.models.LogReportModel;
import com.vinplay.vbee.common.response.BaseResponseModel;
import org.apache.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class TongCuocProcessor implements BaseProcessor<HttpServletRequest, String> {
    private static final Logger logger = Logger.getLogger((String) "backend");

    public String execute(Param<HttpServletRequest> param) {
        BaseResponseModel res = new BaseResponseModel(false, "1001");
        HttpServletRequest request = param.get();
        String nick_name = request.getParameter("nn");
        String fromTime = request.getParameter("ft");
        String endTime = request.getParameter("et");
        if(nick_name == null || nick_name.trim().isEmpty()){
            res.setData(null);
            res.setErrorCode("0");
            res.setSuccess(true);
            return res.toJson();
        }
        AgentDAO dao = new AgentDAOImpl();
        List<UserDetailAgentModel> data = new ArrayList<>();
        TongCuocResponse tongCuocResponse = new TongCuocResponse();
        Long moneyLiveCasino=0L,moneySport=0L,moneyMyGame=0L;
        Long moneyWinCasino=0L,moneyWinSport=0L,moneyWinMyGame=0L;
        Long tongCuoc = 0L, tongThang = 0L;
        try {
            data = dao.getUserDetailAgent(nick_name, fromTime, endTime);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        for(UserDetailAgentModel u:data){
            LogReportModel model = new LogReportModel();
            model.ag = u.getAg();
            model.ag_win = u.getAg_win();
            model.wm = u.getWm();
            model.wm_win = u.getWm_win();
            model.ibc = u.getIbc();
            model.ibc_win = u.getIbc_win();
            model.tlmn =u.getTlmn();
            model.tlmn_win = u.getTlmn_win();
            model.bacay = u.getBacay();
            model.bacay_win = u.getBacay_win();
            model.xocdia = u.getXocdia();
            model.xocdia_win = u.getXocdia_win();
            model.minipoker = u.getMinipoker();
            model.minipoker_win = u.getMinipoker_win();
            model.slot_pokemon = u.getSlot_pokemon();
            model.slot_pokemon_win = u.getSlot_pokemon_win();
            model.baucua = u.getBaucua();
            model.baucua_win = u.getBaucua_win();
            model.taixiu = u.getTaixiu();
            model.taixiu_win = u.getTaixiu_win();
            model.caothap = u.getCaothap();
            model.caothap_win = u.getCaothap_win();
            model.slot_bitcoin = u.getSlot_bitcoin();
            model.slot_bitcoin_win = u.getSlot_bitcoin_win();
            model.slot_taydu = u.getSlot_taydu();
            model.slot_taydu_win = u.getSlot_taydu_win();
            model.slot_angrybird = u.getSlot_angrybird();
            model.slot_angrybird_win = u.getSlot_angrybird_win();
            model.slot_thantai = u.getSlot_thantai();
            model.slot_thantai_win = u.getSlot_thantai_win();
            model.slot_thethao = u.getSlot_thethao();
            model.slot_thethao_win = u.getSlot_thethao_win();
            model.slot_chiemtinh = u.getSlot_chiemtinh();
            model.slot_chiemtinh_win = u.getSlot_chiemtinh_win() == null ? 0 : u.getSlot_chiemtinh_win();
            model.taixiu_st = u.getTaixiu_st() == null ? 0 : u.getTaixiu_st();
            model.taixiu_st_win = u.getTaixiu_st_win() == null ? 0 : u.getTaixiu_st_win();
            model.deposit = u.getDeposit();
            model.withdraw = u.getWithdraw();
            model.slot_bikini = u.getSlot_bikini();
            model.slot_bikini_win = u.getSlot_bikini_win();
            model.slot_galaxy = u.getSlot_galaxy();
            model.slot_galaxy_win = u.getSlot_galaxy_win();

            moneyLiveCasino += model.getMoneyLiveCasino();
            moneyWinCasino += model.getMoneyWinCasino();
            moneySport += model.getMoneySport();
            moneyWinSport += model.getMoneyWinSport();
            moneyMyGame += model.getMoneyMyGame();
            moneyWinMyGame += model.getMoneyWinMyGame();
        }
        tongCuoc = moneyLiveCasino + moneySport + moneyMyGame;
        tongThang = moneyWinCasino + moneyWinSport + moneyWinMyGame;
        tongCuocResponse.setMoneyWinCasino(moneyWinCasino);
        tongCuocResponse.setMoneyWinSport(moneyWinSport);
        tongCuocResponse.setMoneyWinMyGame(moneyWinMyGame);
        tongCuocResponse.setMoneyLiveCasino(moneyLiveCasino);
        tongCuocResponse.setMoneySport(moneySport);
        tongCuocResponse.setMoneyMyGame(moneyMyGame);
        tongCuocResponse.setTongCuoc(tongCuoc);
        tongCuocResponse.setTongThang(tongThang);
        try {
            res.setData(tongCuocResponse);
            res.setErrorCode("0");
            res.setSuccess(true);
        }
        catch (Exception e) {
            // log
        }
        return res.toJson();
    }
}