package com.vinplay.api.processors.hoantra;

import com.vinplay.payment.utils.Constant;
import com.vinplay.usercore.service.UserService;
import com.vinplay.usercore.service.impl.UserServiceImpl;
import com.vinplay.vbee.common.cp.BaseProcessor;
import com.vinplay.vbee.common.cp.Param;
import com.vinplay.vbee.common.models.HoanTraModel;
import com.vinplay.vbee.common.response.BaseResponse;
import org.apache.log4j.Logger;
import javax.servlet.http.HttpServletRequest;
import java.sql.Date;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;

public class SendDataHoanTraProcessor implements BaseProcessor<HttpServletRequest, String> {
    private static final Logger logger = Logger.getLogger("api");

    public String execute(Param<HttpServletRequest> param) {
        HttpServletRequest request = param.get();
        String nickname = request.getParameter("nn");
		String accessToken = request.getParameter("at");
		if(nickname == null || nickname.trim().isEmpty())
			return BaseResponse.error(Constant.ERROR_PARAM, "Nickname is not empty");
		
		if(accessToken == null || accessToken.trim().isEmpty())
			return BaseResponse.error(Constant.ERROR_PARAM, "Access token is not empty");
		
		BaseResponse<Object> res = new BaseResponse<Object>();
		res.setData(null);
		res.setTotalRecords(0);
		res.setErrorCode("1001");
		res.setMessage("error");
		res.setSuccess(false); 
		UserService userService = new UserServiceImpl();
		boolean isToken = userService.isActiveToken(nickname, accessToken);
		if (!isToken) {
			res.setData("Phiên làm việc của bạn đã hết hạn , vui lòng tải lại trang !");
			res.setErrorCode(Constant.ERROR_SESSION);
			return res.toJson();
		}
		
		String dateStr = request.getParameter("date");
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        if (dateStr == null || dateStr.trim().isEmpty()) {
			return BaseResponse.error(Constant.ERROR_PARAM, "Time not empty");
		}
        
        Date date = null;
        try {
        	date = new Date(dateFormat.parse(dateStr).getTime());  
        }catch (Exception e) {
        	res.setData("time is invalid");
			res.setErrorCode(Constant.ERROR_SESSION);
			return res.toJson();
		}
        
        try {
        	//TODO; Check select date is valid
        	Date maxDate = new Date(Date.from(java.time.LocalDate.now().atStartOfDay(java.time.ZoneId.systemDefault()).toInstant()).getTime());
        	Date minDate = new Date(Date.from(java.time.LocalDate.now().plusDays(-7).atStartOfDay(java.time.ZoneId.systemDefault()).toInstant()).getTime());
        	if(date.getTime() >= maxDate.getTime() || dateFormat.parse(dateStr).getTime() < minDate.getTime()) {
        		res.setData("Time have between: " + dateFormat.format(minDate) + " and " + dateFormat.format(maxDate));
    			res.setErrorCode(Constant.ERROR_SESSION);
    			return res.toJson();
        	}
        	
            //TODO: Check history
            if (new ReturnMoney().countListHoanTraHistories(date, nickname) > 0) {
            	res.setErrorCode("1002");
            	res.setData("Bạn đã nhận hoàn trả ngày: " + date.toString());
    			return res.toJson();
            }
            
            //TODO: Validate condition: sumCasino + sumSport + sumGame > 0
            List<HoanTraModel> hoanTraModels = new ReturnMoney().getListHoanTra(date, nickname);
        	long sumCasino = 0; long sumSport = 0; long sumGame = 0;
        	for (HoanTraModel hoantra : hoanTraModels) {
        		sumCasino += hoantra.hoan_tra_casino;
        		sumSport += hoantra.hoan_tra_sport;
        		sumGame += hoantra.hoan_tra_game;
			}
            
        	if((sumCasino + sumSport + sumGame) == 0) {
        		res.setErrorCode("1004");
        		res.setData("Bạn không đủ điều kiện nhận hoàn trả ngày: " + date.toString());
            	return res.toJson();
        	}
            
            // TODO: process add money fund for user play
            return sendHoanTra(date, nickname);
        } catch (Exception e) {
            logger.trace(e);
            return BaseResponse.error("-1", e.getMessage());
        }
    }

    private String sendHoanTra(Date date, String nickname) throws SQLException {
    	//check exist hoan tra
		long count = new ReturnMoney().countListHoanTraHistories(date, nickname);
		if (count > 0) {
			String[] data = new String[] {
					"Bạn đã thực hiện hoàn trả : " + count + " trước đó rồi . Vui lòng kiểm tra lại",
					"Hoàn trả thất bại: " + 0, "Thời gian: " + (date == null ? "all" : date.toLocalDate()) };
			return BaseResponse.success(data, count);
		}
		
        int[] countSendHoanTra = new ReturnMoney().addMoneyHoanTra(date, nickname);
        String [] data = new String[]{
                "Hoàn trả thành công: " + countSendHoanTra[0],
                "Hoàn trả thất bại: " + countSendHoanTra[1],
                "Thời gian: " + (date == null ? "all" : date.toLocalDate())
        };
        
        return BaseResponse.success(data, countSendHoanTra[0]);
    }
}