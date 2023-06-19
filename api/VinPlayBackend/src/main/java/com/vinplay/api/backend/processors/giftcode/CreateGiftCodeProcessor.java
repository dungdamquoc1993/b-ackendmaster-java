package com.vinplay.api.backend.processors.giftcode;

import com.vinplay.giftcode.GiftCodeModel;
import com.vinplay.giftcode.GiftCodeType;
import com.vinplay.giftcode.GiftCodeUtil;
import com.vinplay.usercore.service.UserService;
import com.vinplay.usercore.service.impl.UserServiceImpl;
import com.vinplay.vbee.common.cp.BaseProcessor;
import com.vinplay.vbee.common.cp.Param;
import com.vinplay.vbee.common.response.BaseResponse;
import org.apache.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import java.nio.charset.StandardCharsets;
import java.sql.Timestamp;
import java.util.Base64;
import java.util.Date;
import java.util.Random;

public class CreateGiftCodeProcessor implements BaseProcessor<HttpServletRequest, String> {
    private static final Logger logger = Logger.getLogger((String) "backend");

    public String execute(Param<HttpServletRequest> param) {
        HttpServletRequest request = param.get();

        String amount = request.getParameter("am");
        String nick_name = request.getParameter("nn");
        String type = request.getParameter("type");
        String numberUsed = request.getParameter("nu");
        String startTime = request.getParameter("st");
        String endTime = request.getParameter("et");
        String created_by = request.getParameter("cb");
        String giftCode = request.getParameter("gc");
        String event = request.getParameter("ev");


        if (created_by == null || created_by.isEmpty()) {
            return BaseResponse.error("-1", "cb: Thiếu người tạo");
        }
        
        int money = 0;
        try {
            money = Integer.parseInt(amount);
            if(money <=0){
                return BaseResponse.error("-1", "am: Giá trị giftcode cần > 0");
            } else if(money > 7000000){
                return BaseResponse.error("-1", "am: Giá trị giftcode tối đa là 7 triệu Win");
            }
        } catch (Exception e) {
            return BaseResponse.error("-1", "am: Giá trị giftcode phải là định dạng số");
        }


        Timestamp startDate, endDate;
        if(endTime.compareTo(startTime) <= 0) {
            return BaseResponse.error("-1", "et: hạn gift code cần lớn hơn thời điểm bắt đầu sử dụng");
        } else {
            try {
                long sDate = Long.parseLong(startTime);
                startDate = new Timestamp(sDate);
            } catch (NumberFormatException e) {
                startDate = new Timestamp(new Date().getTime());
            }

            try {
                long eDate = Long.parseLong(endTime);
                endDate = new Timestamp(eDate);

                if(endDate.compareTo(new Timestamp(new Date().getTime())) <= 0) {
                    return BaseResponse.error("-1", "et: hạn gift code cần sau thời điểm hiện tại");
                }
            } catch (NumberFormatException e) {
                //            default expire is 30 day from to day
                long time_bonus = 30L * 24 * 60 * 60 * 1000;
                endDate = new Timestamp(new Date().getTime() + time_bonus);
            }
        }

        try {
            int typeGift = Integer.parseInt(type);

            GiftCodeModel giftCodeModel = new GiftCodeModel();
            giftCodeModel.type = typeGift;
            giftCodeModel.money = money;
            giftCodeModel.max_use = 1;
            giftCodeModel.from = startDate;
            giftCodeModel.exprired = endDate;
            giftCodeModel.giftcode = giftCode;
            giftCodeModel.created_by = created_by;
            switch (typeGift) {
                case GiftCodeType.ONE_FOR_THIS_USER: {
                    if (event == null || event.isEmpty()) {
                        return BaseResponse.error("-1", "ev: Event không được trống");
                    } else {
                        try {
                            giftCodeModel.event = Integer.parseInt(event);
                        } catch (NumberFormatException e) {
                            return BaseResponse.error("-1", "ev: Event phải là số");
                        }
                    }
					if (nick_name == null || nick_name.isEmpty()) {
						return BaseResponse.error("-1", "nn: Nick Name không được trống");
					} else {
						UserService userService = new UserServiceImpl();
						String nn = nick_name.trim();
						if (userService.getUser(nn) == null) {
							return BaseResponse.error("-1", "User này không tồn tại , nn=" + nn);
						}
						giftCodeModel.user_name = nn;
						giftCodeModel.giftcode = giftCode = createRandomGiftCode(4, typeGift, nick_name);
					}
					break;
                }
                case GiftCodeType.ONE_FOR_ONE_USER: {
                    if(numberUsed == null || numberUsed.isEmpty()) {
                        return BaseResponse.error("-1", "nu: Số lần sử dụng không được trống");
                    } else {
                        try {
                            giftCodeModel.max_use = Integer.parseInt(numberUsed);
                            if(giftCodeModel.max_use < 1) {
                                return BaseResponse.error("-1", "nu: Số lần sử dụng tối thiểu là 1");
                            } else if(giftCodeModel.max_use > 100) {
                                return BaseResponse.error("-1", "nu: Số lần sử dụng không thể vượt quá 100");
                            }
                        } catch (NumberFormatException e) {
                            return BaseResponse.error("-1", "nu: Số lần sử dụng phải là số");
                        }
                    }
                    break;
                }
                case GiftCodeType.ONE_FOR_MANY_USER: {
                    break;
                }
                case GiftCodeType.ONE_FOR_ONE_USER_IN_EVENT: {
                    if (event == null || event.isEmpty()) {
                        return BaseResponse.error("-1", "ev: Event không được trống");
                    } else {
                        try {
                            giftCodeModel.event = Integer.parseInt(event);
                        } catch (NumberFormatException e) {
                            return BaseResponse.error("-1", "ev: Event phải là số");
                        }
                    }
                    break;
                }
                default:
                    return BaseResponse.error("-1", "type: Loại giftcode không tồn tại");
            }
//            String giftCodeGen = getGiftCodeValidate(typeGift);
//            giftCodeModel.giftcode = giftCodeGen;

            if (giftCode == null || giftCode.isEmpty()) {
                return BaseResponse.error("-1", "gc: Thiếu gift Code");
            } else if(GiftCodeUtil.giftCodeIsExits(giftCode)) {
                return BaseResponse.error("-1", "gc: Gift code đã tồn tại");
            }
            GiftCodeUtil.insertGiftCode(giftCodeModel);
            return BaseResponse.success(giftCode, 1);
        } catch (Exception e) {
            logger.trace(e);
            return BaseResponse.error("-1", e.getMessage());
        }
    }

    public static final Random rd = new Random();
    private static final String s = "0123456789ABCDEFGHJKMNOPQRSTUVWXYZ";
    public String createRandomGiftCode(int length, int typeGift, String nick_name) {
        String value = typeGift + (nick_name == null || nick_name.isEmpty() ? "LOT": nick_name.substring(0,3).toUpperCase());
        for (int i = 0; i < length; i++) {
            int pos = rd.nextInt(s.length());
            value += s.charAt(pos);
        }
        return value;
    }


}
