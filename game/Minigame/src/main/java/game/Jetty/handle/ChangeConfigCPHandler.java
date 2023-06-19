package game.Jetty.handle;

import bitzero.util.common.business.Debug;
import com.google.gson.Gson;
import com.vinplay.usercore.utils.GameThirdPartyInit;
import com.vinplay.utils.TelegramUtil;
import com.vinplay.vbee.common.statics.Consts;

import game.GameConfig.ConfigGame.MinigameConfig.MinipokerGameConfig;
import game.GameConfig.ConfigGame.MinigameConfig.Slot3x3GameConfig;
import game.GameConfig.ConfigGame.SlotMultiJackpotConfig;
import game.GameConfig.GameConfig;
import game.Jetty.FunctionUtils;
import game.Jetty.JettyErrorCode;
import game.Jetty.JettyResponse;
import game.Jetty.JettyUtils;
import game.Jetty.action.ChangeConfigAction;
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

	private String actionStr(byte action) {
		switch (action) {
            case ChangeConfigAction.GET_MINIPOKER_GAME_CONFIG: {
                return "GET_MINIPOKER_GAME_CONFIG";
            }
            case ChangeConfigAction.SET_MINIPOKER_GAME_CONFIG: {
                return "SET_MINIPOKER_GAME_CONFIG";
            }
            case ChangeConfigAction.GET_SLOT3X3_GAME_CONFIG: {
                return "GET_SLOT3X3_GAME_CONFIG";
            }
            case ChangeConfigAction.SET_SLOT3X3_GAME_CONFIG: {
                return "SET_SLOT3X3_GAME_CONFIG";
            }
            case ChangeConfigAction.GET_MULTI_JACKPOT_GAME_CONFIG: {
                return "GET_MULTI_JACKPOT_GAME_CONFIG";
            }
            case ChangeConfigAction.SET_MULTI_JACKPOT_GAME_CONFIG: {
                return "SET_MULTI_JACKPOT_GAME_CONFIG";
            }
		}
		return action + "";
	}

	public void handle(String target, Request baseRequest, HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
		// get ipaddress
		String ip = getIpAddress(request);
		if (!Consts.IP_SERVER.contains(ip)) {
			return;
		}
        // Validate date from request
        String input = request.getQueryString();
        Map<String, String> params = FunctionUtils.splitQuery(input);
        byte action = JettyUtils.getByte(params.get("action"));
		// notification
		if ("pro".equals(GameThirdPartyInit.enviroment)) {
			TelegramUtil.warningCheat("Setting config , action = " + actionStr(action) + " , param = " + params.toString());
		}
        try {
            switch (action){
                case ChangeConfigAction.GET_MINIPOKER_GAME_CONFIG:{
                    JettyUtils.send(baseRequest, response, new JettyResponse(
                            JettyErrorCode.SUCCESS, gson.toJson(GameConfig.getInstance().minipokerGameConfig)));
                    return;
                }
                case ChangeConfigAction.SET_MINIPOKER_GAME_CONFIG:{
                    String data = params.get("data");
                    if(data.length() <5){
                        JettyUtils.send(baseRequest, response, new JettyResponse(
                                JettyErrorCode.UNKOWN_ERROR, "KhÃ´ng Ä‘Ãºng format Json minipoker config"));
                        return;
                    }
                    try {
                        GameConfig.getInstance().minipokerGameConfig = gson.fromJson(data, MinipokerGameConfig.class);
                        GameConfig.getInstance().setFileConfig("MinipokerGameConfig.json",
                                GameConfig.getInstance().minipokerGameConfig);
                        JettyUtils.send(baseRequest, response, new JettyResponse(
                                JettyErrorCode.SUCCESS, "ok"));
                    }catch (Exception e){
                        Debug.trace(e);
                        JettyUtils.send(baseRequest, response, new JettyResponse(
                                JettyErrorCode.UNKOWN_ERROR, "KhÃ´ng Ä‘Ãºng format Json minipoker config"));
                    }
                    return;
                }
                case ChangeConfigAction.GET_SLOT3X3_GAME_CONFIG:{
                    JettyUtils.send(baseRequest, response, new JettyResponse(
                            JettyErrorCode.SUCCESS, gson.toJson(GameConfig.getInstance().slot3x3GameConfig)));
                    return;
                }
                case ChangeConfigAction.SET_SLOT3X3_GAME_CONFIG:{
                    String data = params.get("data");
                    if(data.length() <5){
                        JettyUtils.send(baseRequest, response, new JettyResponse(
                                JettyErrorCode.UNKOWN_ERROR, "KhÃ´ng Ä‘Ãºng format Json slot3x3 config"));
                        return;
                    }
                    try {
                        GameConfig.getInstance().slot3x3GameConfig = gson.fromJson(data, Slot3x3GameConfig.class);
                        GameConfig.getInstance().setFileConfig("Slot3x3GameConfig.json",
                                GameConfig.getInstance().slot3x3GameConfig);
                        JettyUtils.send(baseRequest, response, new JettyResponse(
                                JettyErrorCode.SUCCESS, "ok"));
                    }catch (Exception e){
                        Debug.trace(e);
                        JettyUtils.send(baseRequest, response, new JettyResponse(
                                JettyErrorCode.UNKOWN_ERROR, "KhÃ´ng Ä‘Ãºng format Json slot3x3 config"));
                    }
                    return;
                }
                case ChangeConfigAction.GET_MULTI_JACKPOT_GAME_CONFIG:{
                    JettyUtils.send(baseRequest, response, new JettyResponse(
                            JettyErrorCode.SUCCESS, gson.toJson(GameConfig.getInstance().slotMultiJackpotConfig)));
                    return;
                }
                case ChangeConfigAction.SET_MULTI_JACKPOT_GAME_CONFIG:{
                    String data = params.get("data");
                    if(data.length() <5){
                        JettyUtils.send(baseRequest, response, new JettyResponse(
                                JettyErrorCode.UNKOWN_ERROR, "KhÃ´ng Ä‘Ãºng format Json multijackpot slot config"));
                        return;
                    }
                    try {
                        GameConfig.getInstance().slotMultiJackpotConfig = gson.fromJson(data, SlotMultiJackpotConfig.class);
                        GameConfig.getInstance().setFileConfig("SlotMultiJackpotConfig.json",
                                GameConfig.getInstance().minipokerGameConfig);
                        JettyUtils.send(baseRequest, response, new JettyResponse(
                                JettyErrorCode.SUCCESS, "ok"));
                    }catch (Exception e){
                        Debug.trace(e);
                        JettyUtils.send(baseRequest, response, new JettyResponse(
                                JettyErrorCode.UNKOWN_ERROR, "KhÃ´ng Ä‘Ãºng format Json  multijackpot slot config"));
                    }
                    return;
                }
            }
        }catch (Exception e){

        }

        JettyUtils.send(baseRequest, response, new JettyResponse(JettyErrorCode.UNKOWN_ERROR, "Ä�Ã£ cÃ³ lá»—i xáº£y ra."));
    }
}
