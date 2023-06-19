package com.vinplay.api.backend.processors.hoantra;

import com.vinplay.dal.dao.AgentDAO;
import com.vinplay.dal.dao.impl.AgentDAOImpl;
import com.vinplay.payment.utils.Constant;
import com.vinplay.vbee.common.cp.BaseProcessor;
import com.vinplay.vbee.common.cp.Param;
import com.vinplay.vbee.common.response.BaseResponse;
import org.apache.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import java.sql.Date;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class CalculateDataHoanTraProcessor implements BaseProcessor<HttpServletRequest, String> {
    private static final Logger logger = Logger.getLogger("backend");

    public String execute(Param<HttpServletRequest> param) {
        HttpServletRequest request = param.get();
        String action = request.getParameter("action");
        String time = request.getParameter("tm");
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        if (time == null || time.trim().isEmpty()) {
			return BaseResponse.error(Constant.ERROR_PARAM, "Time not empty");
		}
		
        String hoanTraAll = request.getParameter("all");
        try {
            int actionCheck = Integer.parseInt(action);

            if(actionCheck == 0){
                // default yesterday
                Date date = time == null || time.isEmpty()
                        ? yesterday() : new Date(Long.parseLong(time));
                AgentDAO dao = new AgentDAOImpl();
                List<String> gameNames = Arrays.asList("ibc", "wm", "ag", "ebet");
                for (String gameName : gameNames) {
                	try{
                		dao.SynDataLogGameToReport(dateFormat.format(date), gameName);
                	}catch (Exception e) { }
				}
                
                return createDataHoanTra(date);
            }else{
                Date date = null;
                if (time == null || time.isEmpty()) {
                    if(hoanTraAll == null || hoanTraAll.isEmpty()) {
                        return BaseResponse.error("-1","tm or all: Thêm ngày hoàn trả hoặc Xác định hoàn trả với tất cả data đã có");
                    } else if(hoanTraAll.equals("1")){
                        //
                    } else {
                        return BaseResponse.error("-1","all: Sai định dạng Xác định hoàn trả với tất cả data đã có");
                    }
                } else {
                    date = new Date(Long.parseLong(time));
                }

                return sendHoanTra(date);
            }

        } catch (Exception e) {
            logger.trace(e);
            return BaseResponse.error("-1", e.getMessage());
        }
    }

    private String createDataHoanTra(Date date) throws SQLException {
        int[] countCreateData = new ReturnMoney().insertHoanTra(date);
        String [] data = new String[]{
                "Thêm mới thành công: " + countCreateData[0],
                "Xóa cũ thành công: " + countCreateData[1],
                "Thời gian: " + (date == null ? "all" : date.toLocalDate())
        };
        return BaseResponse.success(data, countCreateData[0]);
    }

    private String sendHoanTra(Date date) throws SQLException {
    	//check exist hoan tra
		long count = new ReturnMoney().countListHoanTraHistories(date, null);
		if (count > 0) {
			String[] data = new String[] {
					"Bạn đã thực hiện hoàn trả : " + count + " trước đó rồi . Vui lòng kiểm tra lại",
					"Hoàn trả thất bại: " + 0, "Thời gian: " + (date == null ? "all" : date.toLocalDate()) };
			return BaseResponse.success(data, count);
		}
        int[] countSendHoanTra = new ReturnMoney().addMoneyHoanTra(date);
        String [] data = new String[]{
                "Hoàn trả thành công: " + countSendHoanTra[0],
                "Hoàn trả thất bại: " + countSendHoanTra[1],
                "Thời gian: " + (date == null ? "all" : date.toLocalDate())
        };
        return BaseResponse.success(data, countSendHoanTra[0]);
    }

    private static Date yesterday() {
        return new Date(System.currentTimeMillis() - 24 * 60 * 60 * 1000);
    }

}
