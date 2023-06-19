package game.xocdia.server.jetty.handle;

import bitzero.util.common.business.CommonHandle;
import bitzero.util.common.business.Debug;
import com.google.gson.Gson;
import com.vinplay.usercore.utils.GameThirdPartyInit;
import com.vinplay.utils.TelegramUtil;
import com.vinplay.vbee.common.statics.Consts;
import game.xocdia.server.jetty.FunctionUtils;
import game.xocdia.server.jetty.JettyErrorCode;
import game.xocdia.server.jetty.JettyResponse;
import game.xocdia.server.jetty.JettyUtils;
import game.xocdia.server.jetty.action.AdminAction;
import game.xocdia.server.jetty.model.FundData;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.AbstractHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

public class AdminCPHandler extends AbstractHandler {
    private static final Gson gson = new Gson();

    private String getIpAddress(HttpServletRequest request) {
        String ipAddress = request.getHeader("X-FORWARDED-FOR");

        if (ipAddress == null) {
            ipAddress = request.getRemoteAddr();
        }

        String clientIp = null;

        if (ipAddress != null && !"".equals(ipAddress)) {
            String[] arrayIp = ipAddress.split(",");

            if (arrayIp.length > 0) {
                clientIp = arrayIp[0].trim();
            }
        }

        return clientIp;
    }

    public void handle(String target,
                       Request baseRequest,
                       HttpServletRequest request,
                       HttpServletResponse response) throws IOException,
            ServletException {
        // get ipAddress
        String ip = getIpAddress(request);

        if ("pro".equals(GameThirdPartyInit.enviroment) && !Consts.IP_SERVER.contains(ip)) {
            return;
        }

        // Validate date from request
        String input = request.getQueryString();
        Map<String, String> params = FunctionUtils.splitQuery(input);
        byte action = JettyUtils.getByte(params.get("action"));
        // notification
        if ("pro".equals(GameThirdPartyInit.enviroment) && action % 2 != 0) {
            TelegramUtil.warningCheat("Setting config , action = " + action + " , param = " + params.toString());
        }
        try {
            switch (action){
                case AdminAction.GET_FUND_XD:{
                    JettyUtils.send(baseRequest, response, new JettyResponse(JettyErrorCode.SUCCESS,
                            gson.toJson(AdminCPUtils.getFundXD())));
                    return;
                }
                case AdminAction.SET_FUND_XD:{
                    String data = params.get("data");
                    CommonHandle.writeInfoLog("Xocdia jetty: " + data);

                    if(data.length() <5){
                        JettyUtils.send(baseRequest, response, new JettyResponse(
                                JettyErrorCode.UNKOWN_ERROR, "Không đúng format fund XD"));
                        return;
                    }

                    FundData fundData = gson.fromJson(data,FundData.class);
                    AdminCPUtils.setFundXD(fundData);
                    JettyUtils.send(baseRequest, response, new JettyResponse(JettyErrorCode.SUCCESS, "ok"));
                    return;
                }
            }
        }catch (Exception e){
            Debug.trace(e);
        }

        JettyUtils.send(baseRequest, response, new JettyResponse(JettyErrorCode.UNKOWN_ERROR, "Đã có lỗi xảy ra."));
    }
}
