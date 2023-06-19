package com.vinplay.api.processors.events;

import com.vinplay.api.processors.events.response.BuyEventMoonRespinse;
import com.vinplay.dal.entities.event.MoonEventModel;
import com.vinplay.dal.service.EventsService;
import com.vinplay.dal.service.impl.EventsServiceImpl;
import com.vinplay.usercore.service.UserBonusService;
import com.vinplay.usercore.service.UserService;
import com.vinplay.usercore.service.impl.UserBonusServiceImpl;
import com.vinplay.usercore.service.impl.UserServiceImpl;
import com.vinplay.vbee.common.cp.BaseProcessor;
import com.vinplay.vbee.common.cp.Param;
import com.vinplay.vbee.common.enums.Games;
import com.vinplay.vbee.common.models.UserBonusModel;
import com.vinplay.vbee.common.response.MoneyResponse;
import com.vinplay.vbee.common.statics.TransType;
import org.apache.log4j.Logger;

import javax.servlet.http.HttpServletRequest;

public class BuyEventMoonProcessor implements BaseProcessor<HttpServletRequest, String> {
    private Logger logger = Logger.getLogger((String)"api");

    public String execute(Param<HttpServletRequest> param) {
        BuyEventMoonRespinse response = new BuyEventMoonRespinse(false, "1001");
        HttpServletRequest request = (HttpServletRequest)param.get();

        try {
            String ip = getIpAddress(request);
            String nickName = request.getParameter("nn");
            int eventId = Integer.parseInt(request.getParameter("id"));
            EventsService service = new EventsServiceImpl();
            MoonEventModel results = service.buyPackEventMoon(nickName, eventId);
            response.setSuccess(true);
            response.setErrorCode(String.valueOf(results.getErrorCode()));

            if(results.getErrorCode() == 0){
                long bonus = 0;
                UserBonusService userBonusService = new UserBonusServiceImpl();
                UserService userService = new UserServiceImpl();

                switch ((int) results.getAmount()){
                    case 500000:
                        bonus = 99000;
                        break;
                    case 1999000:
                        bonus = 199000;
                        break;
                    case 7999000:
                        bonus = 999000;
                        break;
                    default:
                        bonus = 0;
                        break;
                }

                UserBonusModel model = new UserBonusModel(nickName, eventId, (double) bonus, null, ip, "Khuyến mãi MOON EVENT " + eventId);
                userBonusService.insertBonus(model);

                MoneyResponse res = userService.updateMoney(nickName, bonus, "vin", Games.MOON_NIGHT.getName(),
                        Games.MOON_NIGHT.getId() + "", "MOON_EVENT", 0L,
                        null, TransType.NO_VIPPOINT);

                response.setMoney(res.getCurrentMoney());
            }
        }
        catch (Exception e) {
            this.logger.error((Object)"List event moon Error: ", (Throwable)e);
            return e.getMessage();
        }

        return response.toJson();
    }

    private String getIpAddress(HttpServletRequest request) {
        String ipAddress = request.getHeader("X-FORWARDED-FOR");
        if (ipAddress == null) {
            ipAddress = request.getRemoteAddr();
        }
        return ipAddress;
    }
}
