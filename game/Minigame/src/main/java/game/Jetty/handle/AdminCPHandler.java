package game.Jetty.handle;

import bitzero.server.BitZeroServer;
import bitzero.server.entities.User;
import com.google.gson.Gson;
import com.vinplay.usercore.utils.GameThirdPartyInit;
import com.vinplay.utils.TelegramUtil;
import com.vinplay.vbee.common.statics.Consts;

import game.Jetty.FunctionUtils;
import game.Jetty.JettyErrorCode;
import game.Jetty.JettyResponse;
import game.Jetty.JettyUtils;
import game.Jetty.action.AdminAction;
import game.Jetty.model.FundData;
import game.Jetty.model.FundDataSlot;
import game.Jetty.model.JackpotData;
import game.modules.minigame.BauCuaModule;
import game.modules.minigame.TaiXiuModule;
import game.modules.minigame.room.MGRoomBauCua;
import game.modules.minigame.room.MGRoomTaiXiu;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.AbstractHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
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

	private String actionStr(byte action) {
		switch (action) {
            case AdminAction.GET_JACKPOT_MINIPOKER: {
                return "GET_JACKPOT_MINIPOKER";
            }
            case AdminAction.SET_JACKPOT_MINIPOKER: {
                return "SET_JACKPOT_MINIPOKER";
            }
            case AdminAction.GET_FUND_MINIPOKER: {
                return "GET_FUND_MINIPOKER";
            }
            case AdminAction.SET_FUND_MINIPOKER: {
                return "SET_FUND_MINIPOKER";
            }
            case AdminAction.GET_JACKPOT_SLOT3X3: {
                return "GET_JACKPOT_SLOT3X3";
            }
            case AdminAction.SET_JACKPOT_SLOT3X3: {
                return "SET_JACKPOT_SLOT3X3";
            }
            case AdminAction.GET_FUND_SLOT3X3: {
                return "GET_FUND_SLOT3X3";
            }
            case AdminAction.SET_FUND_SLOT3X3: {
                return "SET_FUND_SLOT3X3";
            }
            case AdminAction.GET_JACKPOT_CAOTHAP: {
                return "GET_JACKPOT_CAOTHAP";
            }
            case AdminAction.SET_JACKPOT_CAOTHAP: {
                return "SET_JACKPOT_CAOTHAP";
            }
            case AdminAction.GET_FUND_CAOTHAP: {
                return "GET_FUND_CAOTHAP";
            }
            case AdminAction.SET_FUND_CAOTHAP: {
                return "SET_FUND_CAOTHAP";
            }
            case AdminAction.GET_FUND_BAUCUA: {
                return "GET_FUND_BAUCUA";
            }
            case AdminAction.SET_FUND_BAUCUA: {
                return "SET_FUND_BAUCUA";
            }
            case AdminAction.GET_FUND_TAIXIU: {
                return "GET_FUND_TAIXIU";
            }
            case AdminAction.SET_FUND_TAIXIU: {
                return "SET_FUND_TAIXIU";
            }
            case AdminAction.GET_CCU: {
                return "GET_CCU";
            }
            case AdminAction.GET_LIST_CCU: {
                return "GET_LIST_CCU";
		    }
            case AdminAction.GET_JACKPOT_GALAXY: {
                return "GET_JACKPOT_GALAXY";
            }
            case AdminAction.SET_JACKPOT_GALAXY: {
                return "SET_JACKPOT_GALAXY";
            }
            case AdminAction.GET_FUND_GALAXY: {
                return "GET_FUND_GALAXY";
            }
            case AdminAction.SET_FUND_GALAXY: {
                return "SET_FUND_GALAXY";
            }
		}
		return action + "";
	}
    public void handle(String target,
                       Request baseRequest,
                       HttpServletRequest request,
                       HttpServletResponse response) throws IOException,
            ServletException {
		// get ipAddress
		String ip = getIpAddress(request);
		if (!Consts.IP_SERVER.contains(ip)) {
			return;
		}
		
        // Validate date from request
        String input = request.getQueryString();
        Map<String, String> params = FunctionUtils.splitQuery(input);
        byte action = JettyUtils.getByte(params.get("action"));
		// notification
		if ("pro".equals(GameThirdPartyInit.enviroment) && action % 2 != 0) {
			TelegramUtil
					.warningCheat("Setting config , action = " + actionStr(action) + " , param = " + params.toString());
		}
        try {
            switch (action) {
                case AdminAction.GET_JACKPOT_MINIPOKER: {
                    JettyUtils.send(baseRequest, response, new JettyResponse(JettyErrorCode.SUCCESS,
                            gson.toJson(AdminCPUtils.getJackpotDataSlotMinipoker())));
                    return;
                }
                case AdminAction.SET_JACKPOT_MINIPOKER: {
                    String data = params.get("data");
                    if (data.length() < 5) {
                        JettyUtils.send(baseRequest, response, new JettyResponse(
                                JettyErrorCode.UNKOWN_ERROR, "Không đúng format jackpot minipoker"));
                        return;
                    }
                    JackpotData jackpotData = gson.fromJson(data, JackpotData.class);
                    AdminCPUtils.setJackpotDataSlotMinipoker(jackpotData);
                    JettyUtils.send(baseRequest, response, new JettyResponse(JettyErrorCode.SUCCESS, "ok"));
                    return;
                }
                case AdminAction.GET_FUND_MINIPOKER: {
                    JettyUtils.send(baseRequest, response, new JettyResponse(JettyErrorCode.SUCCESS,
                            gson.toJson(AdminCPUtils.getFundDataSlotMinipoker())));
                    return;
                }
                case AdminAction.SET_FUND_MINIPOKER: {
                    String data = params.get("data");
                    if (data.length() < 5) {
                        JettyUtils.send(baseRequest, response, new JettyResponse(
                                JettyErrorCode.UNKOWN_ERROR, "Không đúng format fund minipoker"));
                        return;
                    }
                    FundDataSlot fundDataSlot = gson.fromJson(data, FundDataSlot.class);
                    AdminCPUtils.setFundDataSlotMinipoker(fundDataSlot);
                    JettyUtils.send(baseRequest, response, new JettyResponse(JettyErrorCode.SUCCESS, "ok"));
                    return;
                }
                case AdminAction.GET_JACKPOT_SLOT3X3: {
                    JettyUtils.send(baseRequest, response, new JettyResponse(JettyErrorCode.SUCCESS,
                            gson.toJson(AdminCPUtils.getJackpotDataSlot3x3())));
                    return;
                }
                case AdminAction.SET_JACKPOT_SLOT3X3: {
                    String data = params.get("data");
                    if (data.length() < 5) {
                        JettyUtils.send(baseRequest, response, new JettyResponse(
                                JettyErrorCode.UNKOWN_ERROR, "Không đúng format jackpot slot 3x3"));
                        return;
                    }
                    JackpotData jackpotData = gson.fromJson(data, JackpotData.class);
                    AdminCPUtils.setJackpotDataSlot3x3(jackpotData);
                    JettyUtils.send(baseRequest, response, new JettyResponse(JettyErrorCode.SUCCESS, "ok"));
                    return;
                }
                case AdminAction.GET_FUND_SLOT3X3: {
                    JettyUtils.send(baseRequest, response, new JettyResponse(JettyErrorCode.SUCCESS,
                            gson.toJson(AdminCPUtils.getFundDataSlot3x3())));
                    return;
                }
                case AdminAction.SET_FUND_SLOT3X3: {
                    String data = params.get("data");
                    if (data.length() < 5) {
                        JettyUtils.send(baseRequest, response, new JettyResponse(
                                JettyErrorCode.UNKOWN_ERROR, "Không đúng format fund slot 3x3"));
                        return;
                    }
                    FundDataSlot fundDataSlot = gson.fromJson(data, FundDataSlot.class);
                    AdminCPUtils.setFundDataSlot3x3(fundDataSlot);
                    JettyUtils.send(baseRequest, response, new JettyResponse(JettyErrorCode.SUCCESS, "ok"));
                    return;
                }
                case AdminAction.GET_JACKPOT_CAOTHAP: {
                    JettyUtils.send(baseRequest, response, new JettyResponse(JettyErrorCode.SUCCESS,
                            gson.toJson(AdminCPUtils.getJackpotDataCaoThap())));
                    return;
                }
                case AdminAction.SET_JACKPOT_CAOTHAP: {
                    String data = params.get("data");
                    if (data.length() < 5) {
                        JettyUtils.send(baseRequest, response, new JettyResponse(
                                JettyErrorCode.UNKOWN_ERROR, "Không đúng format jackpot cao thap"));
                        return;
                    }
                    JackpotData jackpotData = gson.fromJson(data, JackpotData.class);
                    AdminCPUtils.setJackpotDataCaoThap(jackpotData);
                    JettyUtils.send(baseRequest, response, new JettyResponse(JettyErrorCode.SUCCESS, "ok"));
                    return;
                }
                case AdminAction.GET_FUND_CAOTHAP: {
                    JettyUtils.send(baseRequest, response, new JettyResponse(JettyErrorCode.SUCCESS,
                            gson.toJson(AdminCPUtils.getFundDataCaoThap())));
                    return;
                }
                case AdminAction.SET_FUND_CAOTHAP: {
                    String data = params.get("data");
                    if (data.length() < 5) {
                        JettyUtils.send(baseRequest, response, new JettyResponse(
                                JettyErrorCode.UNKOWN_ERROR, "Không đúng format fund cao thap"));
                        return;
                    }
                    FundData fundData = gson.fromJson(data, FundData.class);
                    AdminCPUtils.setFundDataCaoTHap(fundData);
                    JettyUtils.send(baseRequest, response, new JettyResponse(JettyErrorCode.SUCCESS, "ok"));
                    return;
                }
                case AdminAction.GET_FUND_BAUCUA: {
                    MGRoomBauCua mgRoomBauCua = (MGRoomBauCua)BauCuaModule.getInstance().rooms.get("BauCua_vin_1000");
                    JettyUtils.send(baseRequest, response, new JettyResponse(JettyErrorCode.SUCCESS,
                            gson.toJson(new FundData(mgRoomBauCua.fund))));
                    return;
                }
                case AdminAction.SET_FUND_BAUCUA: {
                    String data = params.get("data");
                    if (data.length() < 5) {
                        JettyUtils.send(baseRequest, response, new JettyResponse(
                                JettyErrorCode.UNKOWN_ERROR, "Không đúng format fund bau cua"));
                        return;
                    }
                    FundData fundData = gson.fromJson(data, FundData.class);
                    MGRoomBauCua mgRoomBauCua = (MGRoomBauCua)BauCuaModule.getInstance().rooms.get("BauCua_vin_1000");
                    mgRoomBauCua.fund = fundData.listFund[0];
                    JettyUtils.send(baseRequest, response, new JettyResponse(JettyErrorCode.SUCCESS, "ok"));
                    return;
                }
                case AdminAction.GET_FUND_TAIXIU: {
                    MGRoomTaiXiu roomTXVin = TaiXiuModule.getInstance().getRoomTX((short)1);
                    JettyUtils.send(baseRequest, response, new JettyResponse(JettyErrorCode.SUCCESS,
                            gson.toJson(new FundData(roomTXVin.getFundTaiXiu()))));
                    return;
                }
                case AdminAction.SET_FUND_TAIXIU: {
                    MGRoomTaiXiu roomTXVin = TaiXiuModule.getInstance().getRoomTX((short)1);
                    String data = params.get("data");
                    if (data.length() < 5) {
                        JettyUtils.send(baseRequest, response, new JettyResponse(
                                JettyErrorCode.UNKOWN_ERROR, "Không đúng format fund tai xiu"));
                        return;
                    }
                    FundData fundData = gson.fromJson(data, FundData.class);
                    roomTXVin.setFundTaiXiu(fundData.listFund[0]);
                    JettyUtils.send(baseRequest, response, new JettyResponse(JettyErrorCode.SUCCESS, "ok"));
                    return;
                }
                case AdminAction.GET_CCU:{
                    int ccu = BitZeroServer.getInstance().getUserManager().getUserCount();
                    response.getWriter().println("user online: "+ccu);
//                    JettyUtils.send(baseRequest, response, new JettyResponse(JettyErrorCode.SUCCESS, ccu + ""));
                    return;
                }
                case AdminAction.GET_LIST_CCU:{
                    List<User> User = BitZeroServer.getInstance().getUserManager().getAllUsers();
                    Gson gson = new Gson();
                    response.getWriter().println(gson.toJson(User));
                    return;
                }
                case AdminAction.GET_JACKPOT_GALAXY: {
                    JettyUtils.send(baseRequest, response, new JettyResponse(JettyErrorCode.SUCCESS,
                            gson.toJson(AdminCPUtils.getJackpotDataGalaxy())));
                    return;
                }
                case AdminAction.SET_JACKPOT_GALAXY: {
                    String data = params.get("data");

                    if (data.length() < 5) {
                        JettyUtils.send(baseRequest, response, new JettyResponse(
                                JettyErrorCode.UNKOWN_ERROR, "Không đúng format jackpot slot 3x3"));
                        return;
                    }

                    JackpotData jackpotData = gson.fromJson(data, JackpotData.class);
                    AdminCPUtils.setJackpotDataGalaxy(jackpotData);
                    JettyUtils.send(baseRequest, response, new JettyResponse(JettyErrorCode.SUCCESS, "ok"));
                    return;
                }
                case AdminAction.GET_FUND_GALAXY: {
                    JettyUtils.send(baseRequest, response, new JettyResponse(JettyErrorCode.SUCCESS,
                            gson.toJson(AdminCPUtils.getFundDataGalaxy())));
                    return;
                }
                case AdminAction.SET_FUND_GALAXY: {
                    String data = params.get("data");

                    if (data.length() < 5) {
                        JettyUtils.send(baseRequest, response, new JettyResponse(
                                JettyErrorCode.UNKOWN_ERROR, "Không đúng format fund slot 3x3"));
                        return;
                    }

                    FundDataSlot fundDataSlot = gson.fromJson(data, FundDataSlot.class);
                    AdminCPUtils.setFundDataGalaxy(fundDataSlot);
                    JettyUtils.send(baseRequest, response, new JettyResponse(JettyErrorCode.SUCCESS, "ok"));
                    return;
                }
				case AdminAction.GET_FUND_JPTX: {
					MGRoomTaiXiu roomTXVin = TaiXiuModule.getInstance().getRoomTX((short) 1);
					long[] funJp = { roomTXVin.getFundJpTai(), roomTXVin.getFundJpXiu() };
					JettyUtils.send(baseRequest, response,
							new JettyResponse(JettyErrorCode.SUCCESS, gson.toJson(new FundData(funJp))));
					return;
				}
                case AdminAction.SET_FUND_JPTAI: {
                	MGRoomTaiXiu roomTXVin = TaiXiuModule.getInstance().getRoomTX((short)1);
                    String data = params.get("data");
                    if (data.length() < 5) {
                        JettyUtils.send(baseRequest, response, new JettyResponse(
                                JettyErrorCode.UNKOWN_ERROR, "Không đúng format fund tai xiu"));
                        return;
                    }
                    FundData fundData = gson.fromJson(data, FundData.class);//{a,b}
                    roomTXVin.setFundJpTai(fundData.listFund[0]);
                    JettyUtils.send(baseRequest, response, new JettyResponse(JettyErrorCode.SUCCESS, "ok"));
                    return;
                }
                case AdminAction.SET_FUND_JPXIU: {
                	MGRoomTaiXiu roomTXVin = TaiXiuModule.getInstance().getRoomTX((short)1);
                    String data = params.get("data");
                    if (data.length() < 5) {
                        JettyUtils.send(baseRequest, response, new JettyResponse(
                                JettyErrorCode.UNKOWN_ERROR, "Không đúng format fund tai xiu"));
                        return;
                    }
                    FundData fundData = gson.fromJson(data, FundData.class);
                    roomTXVin.setFundJpXiu(fundData.listFund[0]);
                    JettyUtils.send(baseRequest, response, new JettyResponse(JettyErrorCode.SUCCESS, "ok"));
                    return;
                }
                case AdminAction.SET_RESULT_JP_TX: {
                	MGRoomTaiXiu roomTXVin = TaiXiuModule.getInstance().getRoomTX((short)1);
                    String data = params.get("data");
                    if (data.length() < 5) {
                        JettyUtils.send(baseRequest, response, new JettyResponse(
                                JettyErrorCode.UNKOWN_ERROR, "Không đúng format fund tai xiu"));
                        return;
                    }
                    FundData fundData = gson.fromJson(data, FundData.class);
                    if(fundData.listFund[0]==0) {
                    	roomTXVin.setFlagJpTai(true);
                        roomTXVin.setFlagJpXiu(false);
                    }else {
                    	roomTXVin.setFlagJpTai(false);
                        roomTXVin.setFlagJpXiu(true);
					}
                    JettyUtils.send(baseRequest, response, new JettyResponse(JettyErrorCode.SUCCESS, "ok"));
                    return;
                }
            }
        } catch (Exception e) {

        }
        JettyUtils.send(baseRequest, response, new JettyResponse(JettyErrorCode.UNKOWN_ERROR, "Da co loi xay ra."));
    }
}
