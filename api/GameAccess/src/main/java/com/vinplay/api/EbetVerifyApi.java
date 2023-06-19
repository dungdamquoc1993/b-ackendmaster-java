package com.vinplay.api;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;

import javax.faces.model.SelectItem;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.vinplay.dto.ebet.EbetUserItem;
import com.vinplay.dto.ebet.RegisterOrLoginReqDto;
import com.vinplay.dto.ebet.RegisterOrLoginRespDto;
import com.vinplay.dto.ebet.UserInfoFromEBetReqDto;
import com.vinplay.dto.ebet.UserInfoFromEBetRespDto;
import com.vinplay.interfaces.ebet.MemberEbetService;
import com.vinplay.logic.CommonMethod;
import com.vinplay.logic.InitData;
import com.vinplay.logic.InitServlet;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vinplay.service.ServiceUtil;
import com.vinplay.usercore.utils.GameThirdPartyInit;

import net.sf.json.JSONObject;

//@WebServlet("/api/test")
public class EbetVerifyApi extends HttpServlet {

	private static final long serialVersionUID = 1L;
	private static Logger logger = Logger.getLogger(EbetVerifyApi.class);

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		doPost(req, res);
	}

//	public static void main(String[] args) {
//		String sign = "mcbOFLGaeYEjE0MFHn7KcjWZ1S/Lm9imVMhQlEDJEjUa3E7llEZ2XezAE9XjYz3AA3RT/NdC9anqwuhKtFBXhg==";
//		String accessToken = "MTU5MTM0MDE0MTBBQmdobEMxREVub0ZtdUc1SGlqa0lKS0xwcXQ5TU5PMlFmdlJTOFRyczRVVnd4V1h5WTM2WmFiY2Q3UGV6dHRPOGo3NGE3VElDRUFudmVsQmVvVG8xMDAz";
//		String data = "1542697113" + accessToken;
//		try {
//			if (CommonMethod.verify(data.getBytes(StandardCharsets.UTF_8), "MFwwDQYJKoZIhvcNAQEBBQADSwAwSAJBAMJ771LHkIGOzXSj9mNCucOnxWw8gUaEhMPK+EpSIcQw5vXMGfocyM1sIH38QSBjkdQkPnNyy3mBFsXwevxPl48CAwEAAQ==", sign)) {
//				logger.info("Success validation!");
//			} else {
//				logger.info("Unsuccess validation!");
//			}
//		} catch (Exception e) {
//			logger.error("e", e);
//		}
//	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		long startTime = System.nanoTime();
		try (InputStream in = request.getInputStream()) {
			String sourcecode = CommonMethod.convInputStream2String(in);
			logger.info("[EBET request params] : " + sourcecode);
			if (sourcecode == null || "".equals(sourcecode)) {
				CommonMethod.printData(response, " {\"msg\":\"Param error!\"}");
				return;
			}
			List<SelectItem> list = CommonMethod.parseParam(sourcecode);
			ServiceUtil.dumpRequestParamListToLog(logger, list);
			if (list == null) {
				CommonMethod.printData(response, " {\"msg\":\"Param error!\"}");
				return;
			}
			String flag = null;
			for (SelectItem si : list) {
				if ("cmd".equals(si.getLabel())) {
					flag = si.getValue().toString();
					break;
				}
			}

			if (flag == null) {
				CommonMethod.printData(response, " {\"msg\":\"Param error!\"}");
				return;
			}

			ObjectMapper mapper = new ObjectMapper();
			if ("RegisterOrLoginReq".equals(flag)) {
				RegisterOrLoginReqDto objReq = mapper.readValue(sourcecode, RegisterOrLoginReqDto.class);
				RegisterOrLoginRespDto objResp = new RegisterOrLoginRespDto();
				if (objReq.getEventType() == 4) {
					String data = objReq.getTimestamp().toString() + objReq.getAccessToken();
					if (CommonMethod.verify(data.getBytes(StandardCharsets.UTF_8), GameThirdPartyInit.PUBLIC_KEY, objReq.getSignature())) {
						EbetUserItem ebetUserItem = MemberEbetService.authUsername(objReq.getUsername(), objReq.getAccessToken());
						if(objReq.getTimestamp() - ebetUserItem.getTimestamp() > 43200) { // 12 hours
							objResp.setStatus(410);
							objResp.setAccessToken("");
							logger.info("[eBET request with cmd RegisterOrLoginReq]: life time of token: " + (objReq.getTimestamp() - ebetUserItem.getTimestamp()));
						} else if (ebetUserItem.getStatus() == 410 || ebetUserItem.getStatus() == 401 || ebetUserItem.getStatus() == 505) {
							objResp.setStatus(ebetUserItem.getStatus());
							objResp.setAccessToken("");
							logger.info("[eBET request with cmd RegisterOrLoginReq]: status: " + ebetUserItem.getStatus());
						} else {
							objResp.setStatus(200);
							objResp.setNickname(ebetUserItem.getLoginname());
							long timeStamp = System.currentTimeMillis() / 1000L;
							String accessToken = CommonMethod.encoding(timeStamp, objReq.getUsername(), ebetUserItem.getPassword());
							objResp.setAccessToken(accessToken);
							logger.info("[eBET request with cmd RegisterOrLoginReq]: status: 200");
						}
					} else {
						objResp.setStatus(4026);
						objResp.setAccessToken("");
						logger.info("[eBET request with cmd RegisterOrLoginReq]: status: 4026");
					}
					String[] sel = new String[] { "log" };
					objResp.setSubChannelId(0);
					objResp.setUsername(objReq.getUsername());
					JSONObject objRegBeanJson = CommonMethod.beanToJSON(objResp, sel);
					CommonMethod.printData(response, objRegBeanJson.toString());
				} else {
					logger.info("[eBET request with cmd RegisterOrLoginReq]: EventType wrong");
					CommonMethod.printData(response, " {\"msg\":\"EventType wrong!\"}");
				}
			}
			if ("UserInfo".equals(flag)) {
				UserInfoFromEBetReqDto objReq = mapper.readValue(sourcecode, UserInfoFromEBetReqDto.class);
				UserInfoFromEBetRespDto objResp = new UserInfoFromEBetRespDto();
				String data = objReq.getUsername() + objReq.getTimestamp();
				if (InitData.isEbetDown()) { // maintenance mode
					objResp.setStatus(505);
					logger.info("Maintenance mode!");
					logger.info("[eBET request with cmd UserInfo]: Maintenance mode!");
				}
				if (CommonMethod.verify(data.getBytes(StandardCharsets.UTF_8), GameThirdPartyInit.PUBLIC_KEY, objReq.getSignature())) {
					logger.info("Success verify!");
					logger.info("[eBET request with cmd UserInfo]: status: 200");
					objResp.setStatus(200);
				} else {
					objResp.setStatus(4026);
					logger.info("[eBET request with cmd UserInfo]: status: 4026");
					logger.info("Unsuccess verify!");
				}
				String[] sel = new String[] { "log" };
				JSONObject objRegBeanJson = CommonMethod.beanToJSON(objResp, sel);
				CommonMethod.printData(response, objRegBeanJson.toString());
			}
		} catch (Exception ex) {
			
			logger.error("ex", ex);
		}
		long endTime = System.nanoTime();
		String message = String.format("Time to complete doPost: %s seconds", CommonMethod.DEC_FORMATTER.format((endTime - startTime) / 1e9));
		logger.info(message);
	}
}
