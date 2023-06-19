//package com.vinplay.logic;
//
//import javax.servlet.ServletException;
//import javax.servlet.http.HttpServlet;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//
//import com.vinplay.dto.gg.*;
//import com.vinplay.dto.ibc2.*;
//import com.vinplay.interfaces.ggaming.*;
//import com.vinplay.interfaces.ibc2.FundTransferIbc2Service;
//import com.vinplay.interfaces.ibc2.MemberIbc2Service;
//
//import org.apache.log4j.Logger;
//
//import com.vinplay.dto.ag.BaseAGResponseDto;
//import com.vinplay.dto.dg.DgCheckUserBalanceReqDto;
//import com.vinplay.dto.dg.DgCheckUserBalanceRespDto;
//import com.vinplay.dto.ebet.EbetUserItem;
//import com.vinplay.dto.ebet.UserInfomationRespDto;
//import com.vinplay.dto.haba.LoginOrCreatePlayerResp;
//import com.vinplay.dto.ibc.CheckUserBalanceReqDto;
//import com.vinplay.dto.ibc.CheckUserBalanceRespDto;
//import com.vinplay.dto.ibc.FundTransferReqDto;
//import com.vinplay.dto.ibc.FundTransferRespDto;
//import com.vinplay.dto.ibc.LoginDto;
//import com.vinplay.interfaces.ag.MemberAGService;
//import com.vinplay.interfaces.dg.FundTransferDgService;
//import com.vinplay.interfaces.dg.MemberDgService;
//import com.vinplay.interfaces.ebet.DepositEbetService;
//import com.vinplay.interfaces.ebet.MemberEbetService;
//import com.vinplay.interfaces.ebet.UserinfoEbetService;
//import com.vinplay.interfaces.ebet.WithdrawEbetService;
//import com.vinplay.interfaces.haba.DepositHabaService;
//import com.vinplay.interfaces.haba.LogoutHabaService;
//import com.vinplay.interfaces.haba.MemberHabaService;
//import com.vinplay.interfaces.haba.QueryPlayerService;
//import com.vinplay.interfaces.haba.QueryTransferService;
//import com.vinplay.interfaces.haba.WithdrawHabaService;
//import com.vinplay.interfaces.ibc.FundTransferIbcService;
//import com.vinplay.interfaces.ibc.LoginIbcService;
//import com.vinplay.interfaces.pt.DepositWithDrawPTService;
//import com.vinplay.interfaces.pt.MemberPTService;
//import com.vinplay.interfaces.sa.MemberSAService;
//import com.vinplay.interfaces.sa.SAFundTransferService;
//import com.vinplay.interfaces.td.GameTDService;
//import com.vinplay.interfaces.td.MemberTDService;
//import com.vinplay.interfaces.td.TDFundTransferService;
//import com.vinplay.item.TDUser;
//import com.vinplay.utils.GlobalConstants;
//import com.vinplay.dto.pt.BaseResponseDto;
//import com.vinplay.dto.pt.CreateUserPTResponseDto;
//import com.vinplay.dto.pt.FunTransferResponseDto;
//import com.vinplay.dto.sa.SALoginDto;
//import com.vinplay.dto.sa.SAUserInfoResp;
//import com.vinplay.dto.td.TDGetPlayerGameURLResp;
//import com.vinplay.dto.td.TDGetUserInfo;
//import com.google.gson.Gson;
//
//import static com.vinplay.logic.InitServlet.IBC2_PLAYER_PREFIX;
//
//public class UIService extends HttpServlet {
//	private static final Logger logger = Logger.getLogger(UIService.class);
//	private static final long serialVersionUID = 1L;
//	//*********************************Ibc**********************************//
//	private LoginIbcService loginIbcService;
//	private Gson gson;
//	private String json;
//	private FundTransferIbcService fundTransferIbcService;
//	private FundTransferIbc2Service fundTransferIbc2Service;
//    private MemberIbc2Service memberIbcService;
//	//*********************************DaGa**********************************
//    private FundTransferDgService fundTransferDgService;
//    private MemberDgService memberDgService;
//	public UIService() {
//		//*********************************Ibc**********************************//
//		loginIbcService = new LoginIbcService();
//		fundTransferIbcService = new FundTransferIbcService();
//		fundTransferIbc2Service = new FundTransferIbc2Service();
//		fundTransferDgService = new FundTransferDgService();
//		memberDgService = new MemberDgService();
//        memberIbcService = new MemberIbc2Service();
//		gson = new Gson();
//	}
//
//	private String getIpAddr(HttpServletRequest request) {
//        String ip = request.getHeader("x-forwarded-for");
//        if ((ip == null) || (ip.length() == 0)
//                || ("unknown".equalsIgnoreCase(ip))) {
//            ip = request.getHeader("Proxy-Client-IP");
//        }
//        if ((ip == null) || (ip.length() == 0)
//                || ("unknown".equalsIgnoreCase(ip))) {
//            ip = request.getHeader("WL-Proxy-Client-IP");
//        }
//        if ((ip == null) || (ip.length() == 0)
//                || ("unknown".equalsIgnoreCase(ip))) {
//            ip = request.getRemoteAddr();
//        }
//        return ip;
//    }
//	
//	public void doPost(HttpServletRequest request, HttpServletResponse response)
//			throws ServletException, java.io.IOException {
//		//logger.info("Thread name of request "+Thread.currentThread().getName());
//		
//		String type = request.getParameter("flag");
//		if (type == null) {
//			return;
//		}
//		String requestIp = getIpAddr(request);
//		if (!GlobalConstants.IP_EBET.contains(requestIp) && !GlobalConstants.IP_ALLOWS.contains(requestIp)) {
//			logger.error("No access permissions, IP = " + requestIp + ", type = " + type);
//			json = "Không có quyền truy cập";
//			return;
//		}
//		try {
//			String PlayerName = request.getParameter("PlayerName");
//			
//			//***********************************IBC-SPORT********************************************//
//			switch (type) {
//				case "login": {
//					LoginDto loginDto = loginIbcService.login(PlayerName);
//					json = gson.toJson(loginDto);
//					break;
//				}
//				case "CheckUserBalance": {
//					CheckUserBalanceReqDto reqDto = new CheckUserBalanceReqDto();
//					reqDto.setPlayerName(PlayerName);
//					CheckUserBalanceRespDto resDto = fundTransferIbcService.CheckUserBalance(reqDto);
//					json = gson.toJson(resDto);
//					break;
//				}
//				case "FundTransfer": {
//					String OpTransId = request.getParameter("OpTransId");
//					double amount = Double.parseDouble(request.getParameter("amount"));
//					int Direction = Integer.parseInt(request.getParameter("Direction"));
//					FundTransferReqDto reqDto = new FundTransferReqDto();
//					reqDto.setPlayerName(PlayerName);
//					reqDto.setAmount(amount);
//					reqDto.setOpTransId(OpTransId);
//					reqDto.setDirection(Direction);
//					FundTransferRespDto resDto = fundTransferIbcService.FundTransfer(reqDto);
//					json = gson.toJson(resDto);
//					//***********************************IBC2-SPORT*********************************************//
//					break;
//				}
//				case "loginIbc2": {//TODO remove
//					LogInRespDto loginDto = memberIbcService.login(PlayerName);
//					json = gson.toJson(loginDto);
//					break;
//				}
//				case "CheckUserBalanceIbc2": {//TODO remove
//					Ibc2CheckUserBalanceReqDto reqDto = new Ibc2CheckUserBalanceReqDto();
//					reqDto.setVendor_member_ids(PlayerName);
//					Ibc2CheckUserBalanceRespDto resDto = memberIbcService.CheckUserBalance(reqDto);
//					json = gson.toJson(resDto);
//					break;
//				}
//				case "FundTransferIbc2"://TODO remove
//					if (!InitData.isIbc2Down()) {
//						String vendorMemberId = request.getParameter("vendor_member_id");
//						String OpTransId = request.getParameter("vendor_trans_id");
//						double amount = Double.parseDouble(request.getParameter("amount"));
//						int currency = Integer.parseInt(request.getParameter("currency"));
//						int Direction = Integer.parseInt(request.getParameter("direction"));
//						int walletId = Integer.parseInt(request.getParameter("wallet_id"));
//						Ibc2FundTransferReqDto reqDto = new Ibc2FundTransferReqDto();
//						reqDto.setVendor_member_id(vendorMemberId);
//						reqDto.setVendor_trans_id(IBC2_PLAYER_PREFIX + OpTransId);
//						reqDto.setAmount(amount);
//						reqDto.setCurrency(currency);
//						reqDto.setDirection(Direction);
//						reqDto.setWallet_id(walletId);
//						Ibc2FundTransferRespDto resDto = fundTransferIbc2Service.FundTransfer(reqDto);
//						json = gson.toJson(resDto);
//					} else {
//						json = "";
//						logger.info("IBC2 was maintained");
//					}
//					//***********************************DaGa*********************************************//
//					break;
//				case "loginDg": {
//					com.vinplay.dto.dg.LogInSessionRespDto loginDto = memberDgService.loginSession(PlayerName);
//					json = gson.toJson(loginDto);
//					break;
//				}
//				case "CheckUserBalanceDg": {
//					DgCheckUserBalanceReqDto reqDto = new DgCheckUserBalanceReqDto();
//					reqDto.setLogin_id(PlayerName);
//					DgCheckUserBalanceRespDto resDto = memberDgService.CheckUserBalance(reqDto);
//					json = gson.toJson(resDto);
//					break;
//				}
//				case "FundTransferDg":
//					if (!InitData.isDgDown()) {
//						String loginId = request.getParameter("login_id");
//						double amount = Double.parseDouble(request.getParameter("amount"));
//						String name = request.getParameter("name");
//						String oddsType = request.getParameter("odds_type");
//						String refNo = request.getParameter("ref_no");
//
//						com.vinplay.dto.dg.CreateMemberReqDto reqDto = new com.vinplay.dto.dg.CreateMemberReqDto();
//						reqDto.setLogin_id(loginId);
//						reqDto.setAmount(amount);
//						reqDto.setName(name);
//						reqDto.setOdds_type(oddsType);
//						reqDto.setRef_no(refNo);
//						com.vinplay.dto.dg.CreateMemberRespeDto resDto = fundTransferDgService.FundTransfer(reqDto);
//						json = gson.toJson(resDto);
//					} else {
//						json = "";
//						logger.info("DG was maintained");
//					}
//
//					break;
//				case "FundWithdrawDg":
//					if (!InitData.isDgDown()) {
//						String loginId = request.getParameter("login_id");
//						double amount = Double.parseDouble(request.getParameter("amount"));
//						String refNo = request.getParameter("ref_no");
//
//						com.vinplay.dto.dg.WithdrawMoneyReqDto reqDto = new com.vinplay.dto.dg.WithdrawMoneyReqDto();
//						reqDto.setLogin_id(loginId);
//						reqDto.setAmount(amount);
//						reqDto.setRef_no(refNo);
//						com.vinplay.dto.dg.WithdrawMoneyRespeDto resDto = fundTransferDgService.withdrawMoney(reqDto);
//						json = gson.toJson(resDto);
//					} else {
//						json = "";
//						logger.info("DG was maintained");
//					}
//
//					break;
//				case "SA_LoginOrCreatePlayer"://FIXME remove
//					if (!InitData.isSADown()) {
//						String loginName = request.getParameter("Username");
//						String ip = request.getParameter("PlayerHostAddress");
//						logger.info("SA_LoginOrCreatePlayer request: " + "Username-" + loginName + " | " + "PlayerHostAddress-" + ip);
//						SALoginDto saUser = MemberSAService.loginMapping(loginName, ip);
//						json = gson.toJson(saUser);
//						logger.info("SA_LoginOrCreatePlayer response: " + json);
//					} else {
//						json = "";
//						logger.info("SA_LoginOrCreatePlayer was maintained");
//					}
//					break;
//				case "SA_GetPlayerInfo": { //TODO remove
//					String loginName = request.getParameter("Username");
//					String playerCode = request.getParameter("playerCode");
//					if (loginName == null || "".equals(loginName) || playerCode == null || "".equals(playerCode)) {
//						json = "";
//						logger.info("SA_GetPlayerInfo loginName or playerCode is null/empty");
//					}else {
//						logger.info("SA_GetPlayerInfo request: " + "Username-" + loginName + "playercode-" + playerCode);
//						MemberSAService service = new MemberSAService();
//						SAUserInfoResp data = service.getPlayerInfo(playerCode);
//						json = gson.toJson(data);
//						logger.info("SA_GetPlayerInfo response: " + json);
//					}
//					break;
//				}
//				case "SA_DepositPlayerMoney"://FIXME remove
//					if (!InitData.isSADown()) {
//						Double amount = Double.parseDouble(request.getParameter("Amount"));
//						String loginName = request.getParameter("Username");
//						String playerCode = request.getParameter("playerCode");
//						logger.info("SA_DepositPlayerMoney request: " + "Amount-" + amount + " | " + "Username-" + loginName + " | playerCode-" + playerCode);
//						SAFundTransferService service = new SAFundTransferService();
//						json = gson.toJson(service.DepositTransfer(amount, loginName, playerCode));
//						logger.info("SA_DepositPlayerMoney response: " + json);
//					} else {
//						json = "";
//						logger.info("SA_DepositPlayerMoney was maintained");
//					}
//
//					break;
//				case "SA_WithdrawPlayerMoney"://FIXME remove
//					if (!InitData.isSADown()) {
//						Double amount = Double.parseDouble(request.getParameter("Amount"));
//						String userName = request.getParameter("Username");
//						String playerCode = request.getParameter("playerCode");
//						logger.info("SA_WithdrawPlayerMoney request: " + "Amount-" + amount + " | " + "Username-" + userName + " | PlayerCode-" + playerCode);
//						SAFundTransferService service = new SAFundTransferService();
//						json = gson.toJson(service.WithdrawTransfer(amount, userName, playerCode));
//						logger.info("SA_WithdrawPlayerMoney response: " + json);
//					} else {
//						json = "";
//						logger.info("SA_WithdrawPlayerMoney was maintained");
//					}
//
//					break;
//				//*********************************** END SA  *********************************************//
//
//				//***********************************HABANERO*********************************************//
//				case "Haba_LoginOrCreatePlayer"://TODO remove
//					if (!InitData.isHabaDown()) {
//						String loginName = request.getParameter("Username");
//						String ip = request.getParameter("PlayerHostAddress");
//						String userAgent = request.getParameter("UserAgent");
//						logger.info("Haba_LoginOrCreatePlayer request: " + "Username-" + loginName + " | " + "PlayerHostAddress-" + ip + " | " + "UserAgent-" + userAgent);
//						LoginOrCreatePlayerResp loginHaba = MemberHabaService.loginMapping(loginName, ip, userAgent);
//						json = gson.toJson(loginHaba);
//						logger.info("Haba_LoginOrCreatePlayer response: " + json);
//					} else {
//						json = "";
//						logger.info("Haba_LoginOrCreatePlayer was maintained");
//					}
//
//					break;
//				case "Haba_DepositPlayerMoney"://TODO remove
//					if (!InitData.isHabaDown()) {
//						Double amount = Double.parseDouble(request.getParameter("Amount"));
//						String loginName = request.getParameter("Username");
//						String ip = request.getParameter("Ip");
//						logger.info("Haba_DepositPlayerMoney request: " + "Amount-" + amount + " | " + "Username-" + loginName + " | Ip-" + ip);
//						DepositHabaService depositHaba = new DepositHabaService();
//						json = gson.toJson(depositHaba.DepositTransfer(amount, loginName, ip));
//						logger.info("Haba_DepositPlayerMoney response: " + json);
//					} else {
//						json = "";
//						logger.info("Haba_DepositPlayerMoney was maintained");
//					}
//
//					break;
//				case "Haba_WithdrawPlayerMoney"://TODO remove
//					if (!InitData.isHabaDown()) {
//						Double amount = Double.parseDouble(request.getParameter("Amount"));
//						String userName = request.getParameter("Username");
//						String ip = request.getParameter("Ip");
//						logger.info("Haba_WithdrawPlayerMoney request: " + "Amount-" + amount + " | " + "Username-" + userName + " | Ip-" + ip);
//						WithdrawHabaService withdrawHaba = new WithdrawHabaService();
//						json = gson.toJson(withdrawHaba.WithdrawTransfer(amount, userName, ip));
//						logger.info("Haba_WithdrawPlayerMoney response: " + json);
//					} else {
//						json = "";
//						logger.info("Haba_WithdrawPlayerMoney was maintained");
//					}
//
//					break;
//				case "Haba_QueryTransfer"://TODO remove dont use
//					if (!InitData.isHabaDown()) {
//						String requestId = request.getParameter("RequestId");
//						logger.info("Haba_QueryTransfer request: " + "RequestId-" + requestId);
//						QueryTransferService service = new QueryTransferService();
//						json = gson.toJson(service.QueryTranfer(requestId));
//						logger.info("Haba_QueryTransfer response: " + json);
//					} else {
//						json = "";
//						logger.info("Haba_QueryTransfer was maintained");
//					}
//
//					break;
//				case "Haba_QueryPlayer": {//TODO remove
//					String userName = request.getParameter("Username");
//					logger.info("Haba_QueryPlayer request: " + "Username-" + userName);
//					QueryPlayerService service = new QueryPlayerService();
//					json = gson.toJson(service.QueryPlayer(userName));
//					logger.info("Haba_QueryPlayer response: " + json);
//					break;
//				}
//				case "Haba_LogOutPlayer": {//TODO remove dont use
//					String userName = request.getParameter("Username");
//					logger.info("Haba_LogOutPlayer request: " + "Username-" + userName);
//					LogoutHabaService service = new LogoutHabaService();
//					json = gson.toJson(service.LogOutPlayer(userName));
//					logger.info("Haba_LogOutPlayer response: " + json);
//					break;
//				}
//				case "Haba_LogoutAllPlayersInBrand": {//TODO remove dont use
//					logger.info("Haba_LogoutAllPlayersInBrand request: " + "No param");
//					LogoutHabaService service = new LogoutHabaService();
//					json = gson.toJson(service.LogoutAllPlayersInBrand());
//					logger.info("Haba_LogoutAllPlayersInBrand response: " + json);
//					break;
//				}
//				//***********************************eBET*********************************************//
//				case "Ebet_LauchingURL": {//TODO remove
//					String loginName = request.getParameter("Username");
//					logger.info("Ebet_LauchingURL request: " + "Username-" + loginName);
//					EbetUserItem ebetUserItem = MemberEbetService.getEbetId(loginName);
//					json = gson.toJson(ebetUserItem);
//					logger.info("Ebet_LauchingURL response: " + json);
//					break;
//				}
//				case "Ebet_DepositPlayerMoney"://TODO remove
//					if (!InitData.isEbetDown()) {
//						Double amount = Double.parseDouble(request.getParameter("Amount"));
//						String loginName = request.getParameter("Username");
//						String ip = request.getParameter("Ip");
//						logger.info("Ebet_DepositPlayerMoney request: " + "Amount-" + amount + " | " + "Username-" + loginName + " | Ip-" + ip);
//						DepositEbetService depositEbet = new DepositEbetService();
//						json = gson.toJson(depositEbet.DepositTransfer(amount, loginName, ip));
//						logger.info("Ebet_DepositPlayerMoney response: " + json);
//					} else {
//						json = "";
//						logger.info("Ebet_DepositPlayerMoney was maintained");
//					}
//
//					break;
//				case "Ebet_WithdrawPlayerMoney"://TODO remove
//					if (!InitData.isEbetDown()) {
//						Double amount = Double.parseDouble(request.getParameter("Amount"));
//						String userName = request.getParameter("Username");
//						String ip = request.getParameter("Ip");
//						logger.info("Ebet_WithdrawPlayerMoney request: " + "Amount-" + amount + " | " + "Username-" + userName + " | Ip-" + ip);
//						WithdrawEbetService withdrawEbet = new WithdrawEbetService();
//						json = gson.toJson(withdrawEbet.WithdrawTransfer(amount, userName, ip));
//						logger.info("Ebet_WithdrawPlayerMoney response: " + json);
//					} else {
//						json = "";
//						logger.info("Ebet_WithdrawPlayerMoney was maintained");
//					}
//					break;
//				case "Ebet_UserInfo": {//TODO remove
//					String loginName = request.getParameter("Username");
//					logger.info("Ebet_UserInfo request: " + "Username-" + loginName);
//					UserinfoEbetService service = new UserinfoEbetService();
//					UserInfomationRespDto userInfo = service.GetUserInfo(loginName);
//					json = gson.toJson(userInfo);
//					logger.info("Ebet_UserInfo response: " + json);
//
//					break;
//				}
//				//***********************************GG-BanCa*********************************************//
//				case "GG_GetBalance": {//FIXME remove
//					String loginName = request.getParameter("Username");
//					GetBalanceService getBalanceService = new GetBalanceService();
//					GetBalanceResDto resDto = getBalanceService.getBalance(loginName);
//					json = gson.toJson(resDto);
//					logger.info("GG_GetBalance response: " + json);
//					break;
//				}
//				case "GG_ForwardGame": {//FIXME remove
//					String loginName = request.getParameter("Username");
//					Integer gameType = Integer.parseInt(request.getParameter("gameType"));
//					String ip = request.getParameter("Ip");
//					String isApp = request.getParameter("isapp");
//					ForwardGameService forwardGameService = new ForwardGameService();
//					ForwardGameResDto resDto = forwardGameService.forwardGame(loginName, gameType, ip, isApp);
//					json = gson.toJson(resDto);
//					logger.info("GG_GetBalance response: " + json);
//					break;
//				}
//				case "GG_KillSession": {//FIXME remove
//					String loginName = request.getParameter("Username");
//					KillSessionService killSessionService = new KillSessionService();
//					KillSessionResDto resDto = killSessionService.killSession(loginName);
//					json = gson.toJson(resDto);
//					logger.info("GG_KillSession response: " + json);
//					break;
//				}
//				case "GG_TransferCredit"://FIXME remove
//					if (!InitData.isGGDown()) {
//						String loginName = request.getParameter("Username");
//						Double amount = Double.parseDouble(request.getParameter("Amount"));
//						String typeTransfer = request.getParameter("Type");
//						String ip = request.getParameter("Ip");
//						TransferCreditService transferCreditService = new TransferCreditService();
//						TransferCreditResDto resDto = transferCreditService.transferCredit(loginName, amount, typeTransfer, ip);
//						json = gson.toJson(resDto);
//						logger.info("GG_TransferCredit response: " + json);
//					} else {
//						json = "";
//						logger.info("GG was maintained");
//					}
//
//
//					// *********************************** TD Skywind *********************************************//
//					break;
//				case "TD_LoginOrCreatePlayer":
//					if (!InitData.isTDDown()) {
//						String loginName = request.getParameter("Username");
//						String ip = request.getParameter("PlayerHostAddress");
//						logger.info("TD_LoginOrCreatePlayer request: " + "Username-" + loginName + " | " + "PlayerHostAddress-" + ip);
//						TDUser tdUser = MemberTDService.loginMapping(loginName, ip);
//						json = gson.toJson(tdUser);
//						logger.info("TD_LoginOrCreatePlayer response: " + json);
//					} else {
//						json = "";
//						logger.info("TD_LoginOrCreatePlayer maintain ");
//					}
//
//
//					break;
//				case "TD_GetGameLaunchURL":
//					if (!InitData.isTDDown()) {
//						String loginName = request.getParameter("Username");
//						String gameCode = request.getParameter("gameCode");
//						String playerCode = request.getParameter("playerCode");
//						if(playerCode==null || "".equals(playerCode)) {
//							json = "";
//							logger.info("playerCode is null or empty , loginName= " + loginName + ", gameCode=" + gameCode);
//						}else {
//							logger.info("TD_GetGameLaunchURL request: " + "Username-" + loginName + " | " + "gamecode-" + gameCode + "playercode-" + playerCode);
//							GameTDService service = new GameTDService();
//							TDGetPlayerGameURLResp data = service.getGameLaunchURL(loginName, gameCode, playerCode);
//							json = gson.toJson(data);
//							logger.info("TD_GetGameLaunchURL response: " + json);
//						}
//						
//					} else {
//						json = "";
//						logger.info("TD_GetGameLaunchURL maintain ");
//					}
//
//
//					break;
//				case "TD_GetPlayerInfo"://FIXME remove
//					if (!InitData.isTDDown()) {
//						String loginName = request.getParameter("Username");
//						String playerCode = request.getParameter("playerCode");
//						logger.info("TD_GetPlayerInfo request: " + "Username-" + loginName + "playercode-" + playerCode);
//						GameTDService service = new GameTDService();
//						TDGetUserInfo data = service.getUserInfo(loginName, playerCode);
//						json = gson.toJson(data);
//						logger.info("TD_GetPlayerInfo response: " + json);
//					} else {
//						json = "";
//						logger.info("TD_GetPlayerInfo maintain ");
//					}
//
//					break;
//				case "TD_DepositPlayerMoney"://FIXME remove
//					if (!InitData.isTDDown()) {
//						Double amount = Double.parseDouble(request.getParameter("Amount"));
//						String loginName = request.getParameter("Username");
//						String playerCode = request.getParameter("playerCode");
//						logger.info("TD_DepositPlayerMoney request: " + "Amount-" + amount + " | " + "Username-" + loginName + " | playerCode-" + playerCode);
//						TDFundTransferService service = new TDFundTransferService();
//						json = gson.toJson(service.DepositTransfer(amount, loginName, playerCode));
//						logger.info("TD_DepositPlayerMoney response: " + json);
//					} else {
//						json = "";
//						logger.info("TD_DepositPlayerMoney maintain ");
//					}
//
//					break;
//				case "TD_WithdrawPlayerMoney":
//					if (!InitData.isTDDown()) {
//						Double amount = Double.parseDouble(request.getParameter("Amount"));
//						String userName = request.getParameter("Username");
//						String playerCode = request.getParameter("playerCode");
//						logger.info("TD_WithdrawPlayerMoney request: " + "Amount-" + amount + " | " + "Username-" + userName + " | PlayerCode-" + playerCode);
//						TDFundTransferService service = new TDFundTransferService();
//						json = gson.toJson(service.WithdrawTransfer(amount, userName, playerCode));
//						logger.info("TD_WithdrawPlayerMoney response: " + json);
//					} else {
//						json = "";
//						logger.info("TD_WithdrawPlayerMoney maintain ");
//					}
//
//
//					break;
//				// ***********************************TDSkywind END*********************************************//
//					/********************************* PT Game *******************************/
//				case "PT_UserInfo": {//FIXME remove
//					String ptPlayerName = request.getParameter("ptPlayerName");
//					int isRealTime = Integer.parseInt(request.getParameter("isRealTime"));
//					logger.info("PT_UserInfo request: " + "player name -" + ptPlayerName);
//					MemberPTService service = new MemberPTService();
//					BaseResponseDto<CreateUserPTResponseDto> userInfo = service.getPlayerInfoByName(ptPlayerName, isRealTime);
//					json = gson.toJson(userInfo);
//					logger.info("PT_UserInfo response: " + json);
//					break;
//				}
//				case "PT_Deposit"://FIXME remove
//					if (!InitData.isPTDown()) {
//						DepositWithDrawPTService service = new DepositWithDrawPTService();
//						Double amount = Double.parseDouble(request.getParameter("Amount"));
//						String loginName = request.getParameter("Username");
//						String ip = request.getParameter("Ip");
//						logger.info("PT_Deposit Player Money request: " + "Amount-" + amount + " | " + "Username-" + loginName + " | Ip-" + ip);
//						BaseResponseDto<FunTransferResponseDto> depositReponse = service.depositTransfer(amount, loginName, ip);
//						json = gson.toJson(depositReponse);
//						logger.info("PT_Deposit Player Money response: " + json);
//					} else {
//						json = "";
//						logger.info("PT was maintained");
//					}
//
//					break;
//				case "PT_Withdraw"://FIXME remove
//					if (!InitData.isPTDown()) {
//						DepositWithDrawPTService service = new DepositWithDrawPTService();
//						Double amount = Double.parseDouble(request.getParameter("Amount"));
//						String loginName = request.getParameter("Username");
//						String ip = request.getParameter("Ip");
//						logger.info("PT_Withdraw Player Money request: " + "Amount-" + amount + " | " + "Username-" + loginName + " | Ip-" + ip);
//						BaseResponseDto<FunTransferResponseDto> withdrawResponse = service.withdrawTransfer(amount, loginName, ip);
//						json = gson.toJson(withdrawResponse);
//						logger.info("PT_Withdraw Player Money response: " + json);
//					} else {
//						json = "";
//						logger.info("PT was maintained");
//					}
//
//					break;
//				// ***********************************AG*********************************************//
//				case "AG_GetBalance": {//FIXME remove
//					String loginName = request.getParameter("loginname");
//					logger.info("AG_GetBalance request: " + "login name -" + loginName);
//					MemberAGService service = new MemberAGService();
//					BaseAGResponseDto responseAG = service.checkBalance(loginName);
//					json = gson.toJson(responseAG);
//					logger.info("AG_GetBalance response: " + json);
//					break;
//				}
//				case "AG_ForwardGame": {
//					String loginName = request.getParameter("loginname");
//					String mh5 = request.getParameter("mh5");
//					logger.info("AG_ForwardGame request: " + "login name -" + loginName + "mobile -" + mh5);
//					MemberAGService service = new MemberAGService();
//					json = service.forwardGame(loginName, mh5);
//					logger.info("AG_ForwardGame response: " + json);
//					break;
//				}
//				case "AG_Deposit"://FIXME remove
//					if (!InitData.isAGDown()) {
//						MemberAGService service = new MemberAGService();
//						Double amount = Double.parseDouble(request.getParameter("Amount"));
//						String loginName = request.getParameter("Username");
//						String ip = request.getParameter("Ip");
//						logger.info("AG_Deposit Player Money request: " + "Amount-" + amount + " | " + "Username-" + loginName
//								+ " | Ip-" + ip);
//						BaseAGResponseDto depositReponse = service.depositTransfer(loginName, amount, ip);
//						json = gson.toJson(depositReponse);
//						logger.info("AG_Deposit Player Money response: " + json);
//					} else {
//						json = "";
//						logger.info("Ag was maintained");
//					}
//
//					break;
//				case "AG_Withdraw"://FIXME remove
//					if (!InitData.isAGDown()) {
//						MemberAGService service = new MemberAGService();
//						Double amount = Double.parseDouble(request.getParameter("Amount"));
//						String loginName = request.getParameter("Username");
//						String ip = request.getParameter("Ip");
//						logger.info("AG_Withdraw Player Money request: " + "Amount-" + amount + " | " + "Username-" + loginName
//								+ " | Ip-" + ip);
//						BaseAGResponseDto withdrawResponse = service.withdrawTransfer(loginName, amount, ip);
//						json = gson.toJson(withdrawResponse);
//						logger.info("AG_Withdraw Player Money response: " + json);
//					} else {
//						json = "";
//						logger.info("Ag was maintained");
//					}
//
//					break;
////				case "HABA_JACKPOT":
////					if (!InitData.isHabaDown()) {
////						//List<HabaJackpotDto> lstResult = InitData.getLstHabaJackpot();
////						json = InitData.getLstHabaJackpotStr();
////						logger.info("HABA_JACKPOT response: " + json);
////					} else {
////						json = "";
////						logger.info("HABA_JACKPOT was maintained");
////					}
////
////					break;
//			}
//			//==
//			response.setContentType("application/json");
//			response.setCharacterEncoding("UTF-8");
//			response.getWriter().write(json);
//		} catch (Exception ex) {
//			logger.error(type + ":", ex);
//		}
//	}
//
//	public void doGet(HttpServletRequest request, HttpServletResponse response)
//			throws ServletException, java.io.IOException {
//		doPost(request, response);
//	}
//}
