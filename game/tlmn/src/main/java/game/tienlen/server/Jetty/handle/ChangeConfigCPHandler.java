package game.tienlen.server.Jetty.handle;

import bitzero.util.common.business.Debug;
import com.google.gson.Gson;
import com.vinplay.usercore.utils.GameThirdPartyInit;
import com.vinplay.utils.TelegramUtil;
import com.vinplay.vbee.common.statics.Consts;
import game.tienlen.server.GameConfig.Config.TLMNBotConfig;
import game.tienlen.server.GameConfig.GameConfig;
import game.tienlen.server.Jetty.FunctionUtils;
import game.tienlen.server.Jetty.JettyErrorCode;
import game.tienlen.server.Jetty.JettyResponse;
import game.tienlen.server.Jetty.JettyUtils;
import game.tienlen.server.Jetty.action.ChangeConfigAction;
import game.xocdia.conf.XocDiaConfig;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.AbstractHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;


public class ChangeConfigCPHandler extends AbstractHandler {
    private Gson gson = new Gson();

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
		// get ipaddress
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
                case ChangeConfigAction.GET_TLMN_CONFIG:{
                    JettyUtils.send(baseRequest, response, new JettyResponse(
                            JettyErrorCode.SUCCESS, gson.toJson(GameConfig.getInstance().tlmnBotConfig)));
                    return;
                }
                case ChangeConfigAction.SET_TLMN_CONFIG:{
                    String data = params.get("data");

                    if(data.length() <5){
                        JettyUtils.send(baseRequest, response, new JettyResponse(
                                JettyErrorCode.UNKOWN_ERROR, "Không đúng format TLMN"));
                        return;
                    }

                    try {
                        GameConfig.getInstance().tlmnBotConfig = gson.fromJson(data, TLMNBotConfig.class);
                        GameConfig.getInstance().setFileConfig("TLMNBotConfig.json", GameConfig.getInstance().tlmnBotConfig);
                        JettyUtils.send(baseRequest, response, new JettyResponse(
                                JettyErrorCode.SUCCESS, "ok"));
                    }catch (Exception e){
                        Debug.trace(e);
                        JettyUtils.send(baseRequest, response, new JettyResponse(
                                JettyErrorCode.UNKOWN_ERROR, "Không đúng format TLMN"));
                    }

                    return;
                }
            }
        }catch (Exception e){
            Debug.trace(e);
        }

        JettyUtils.send(baseRequest, response, new JettyResponse(JettyErrorCode.UNKOWN_ERROR, "Đã có lỗi xảy ra."));
    }
}
