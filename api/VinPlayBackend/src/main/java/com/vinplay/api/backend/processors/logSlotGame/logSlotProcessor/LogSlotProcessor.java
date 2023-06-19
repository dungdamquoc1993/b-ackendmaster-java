package com.vinplay.api.backend.processors.logSlotGame.logSlotProcessor;

import com.vinplay.api.backend.processors.logSlotGame.logSlotModel.LogSlotModel;
import com.vinplay.api.backend.processors.logSlotGame.SlotLogController;
import com.vinplay.vbee.common.cp.BaseProcessor;
import com.vinplay.vbee.common.cp.Param;
import org.apache.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

public class LogSlotProcessor implements BaseProcessor<HttpServletRequest, String> {
    private static final Logger logger = Logger.getLogger("backend");

    public String execute(Param<HttpServletRequest> param) {
        ResultSlotResponse response = new ResultSlotResponse(false, "1001");
        HttpServletRequest request = param.get();
        String nickName = request.getParameter("nn");
        String transId = request.getParameter("tid");
        String bet_value = request.getParameter("r");
        String timeStart = request.getParameter("ts");
        String timeEnd = request.getParameter("te");
        //String gameType = request.getParameter("gt");
        int page = 1;
        try {
            page = Integer.parseInt(request.getParameter("p"));
            if (page < 1) {
                response.setMessage("page cần lớn hơn 0");
                return response.toJson();
            }
        }catch (NumberFormatException e) {
            page = 1;
        }

        int limit = 50;
        try {
            limit = Integer.parseInt(request.getParameter("l"));
            if (limit < 1) {
                response.setMessage("limit cần lớn hơn 0");
                return response.toJson();
            }
        }catch (NumberFormatException e) {
            limit = 50;
        }
        Integer gameType = null;
        try {
        	gameType = Integer.parseInt(request.getParameter("gameType"));
		} catch (Exception e) {
			return "gameType is not a number";
		}
        try{
            List<LogSlotModel> trans = SlotLogController.getInstance().logSlotDAO.getListSlot(nickName, transId,
                    bet_value, timeStart, timeEnd, gameType, page, limit);
            long totalRecord = SlotLogController.getInstance().logSlotDAO.countListSlot(nickName, transId,
                    bet_value, timeStart, timeEnd, gameType);
            long totalPages = (int) Math.ceil((double) totalRecord / limit);
            Map<String, Long> map = SlotLogController.getInstance().logSlotDAO.calculatorTotalCuocThang(nickName, transId,
                    bet_value, timeStart, timeEnd, gameType);
            long tong_cuoc = map.get("tong_cuoc");
            long tong_thang = map.get("tong_thang");
            long tong_player = map.get("tong_player");
            response.setTong_cuoc(tong_cuoc);
            response.setTong_thang(tong_thang);
            response.setTong_player(tong_player);
            response.setTotal(totalPages);
            response.setTotalRecord(totalRecord);
            response.setTransactions(trans);
            response.setSuccess(true);
            response.setErrorCode("0");
        }catch (Exception e){
            e.printStackTrace();
            logger.debug(e);
        }
        return response.toJson();
    }
}
