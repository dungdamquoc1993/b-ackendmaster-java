//package com.vinplay.api.backend.processors.logSlotGame.logSlotGame;
//
//import com.vinplay.api.backend.processors.logSlotGame.logSlotModel.LogSlotDAO;
//import com.vinplay.api.backend.processors.logSlotGame.logSlotModel.LogSlotModel;
//import com.vinplay.vbee.common.enums.Games;
//
//import java.util.List;
//
//public class LogSlotTamHung extends LogSlotDAO {
//    public List<LogSlotModel> getListSlot(String nickName, String transId, String bet_value,
//                                                 String timeStart, String timeEnd, String moneyType, int page){
//
//        return this.getListSlot(nickName, transId,bet_value,timeStart,timeEnd,moneyType, page,"log_" + Games.TAMHUNG.getName());
//    }
//}
