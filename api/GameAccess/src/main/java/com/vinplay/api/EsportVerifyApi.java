///**
// * Archie
// */
//package com.vinplay.api;
//
//import java.io.IOException;
//import java.io.InputStream;
//import java.util.List;
//import java.util.Optional;
//
//import javax.faces.model.SelectItem;
//import javax.servlet.ServletException;
//import javax.servlet.annotation.WebServlet;
//import javax.servlet.http.HttpServlet;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//
//import org.apache.commons.lang.StringUtils;
//import org.apache.log4j.Logger;
//
//import com.vinplay.dto.esport.EsportLoginBase64;
//import com.vinplay.dto.esport.EsportVerifyResponse;
//import com.vinplay.logic.CommonMethod;
//import com.vinplay.logic.InitServlet;
//import com.vinplay.service.GamesCommonService;
//import com.vinplay.service.esport.EsportService;
//import com.vinplay.service.esport.ipml.EsportServiceImpl;
//import com.vinplay.utils.BaseResponse;
//import com.vinplay.utils.GlobalConstants;
//import com.vinplay.utils.HttpUtils;
//import com.vinplay.utils.MD5Utils;
//import com.fasterxml.jackson.databind.ObjectMapper;
//
///**
// * @author Archie
// *
// */
//@WebServlet(name = "EsportVerifyApi", description = "API for verify request from Esport", urlPatterns = { "/api/esport/verify" })
//public class EsportVerifyApi extends HttpServlet {
//
//	private static final long serialVersionUID = 1L;
//	private static final Logger logger = Logger.getLogger(EsportVerifyApi.class);
//
//	@Override
//	protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
//		doPost(req, res);
//	}
//
//	@Override
//	protected void doPost(HttpServletRequest request, HttpServletResponse response)
//			throws ServletException, IOException {
//		
//		EsportVerifyResponse esportVerifyResponse = new EsportVerifyResponse();
//		esportVerifyResponse.setLoginName("");
//		
//		String ipAddress = HttpUtils.getIpAddress(request);
//		if("dev".equals(InitServlet.enviroment)) {
//			if (!GlobalConstants.IP_ESPORT_STAGING.contains(ipAddress)) {
//				logger.error("Please check your IP Address again ,ipaddress = " + ipAddress);
//				sendResponse(response, esportVerifyResponse);
//				return;
//			}
//		}else {
//			if (!GlobalConstants.IP_ESPORT_PRO.contains(ipAddress)) {
//				logger.error("Please check your IP Address again ,ipaddress = " + ipAddress);
//				sendResponse(response, esportVerifyResponse);
//				return;
//			}
//		}
//		
//		long startTime = System.nanoTime();
//		EsportService service =new EsportServiceImpl();
//		try (InputStream in = request.getInputStream()){
//			
//			String sourcecode = CommonMethod.convInputStream2String(in);
//			logger.info("[EsportVerifyApi request params] : " + sourcecode);
//			if (sourcecode == null || "".equals(sourcecode)) {
//				sendResponse(response, esportVerifyResponse);
//				return;
//			}
//			List<SelectItem> list = CommonMethod.parseParam(sourcecode);
//			String token = null;
//			for (SelectItem si : list) {
//				if ("token".equals(si.getLabel())) {
//					token = si.getValue().toString();
//					break;
//				}
//			}
//			logger.info("[EsportVerifyApi request params] : " + token);
//			if (StringUtils.isBlank(token)) {
//				logger.error("EsportVerifyApi Params is null or empty");
//				sendResponse(response, esportVerifyResponse);
//				return;
//			}
//			Optional<EsportLoginBase64> esportLoginBase64 = service.esportDecode(token);
//			if (esportLoginBase64.isPresent()) {
//				String ourSecretKey = esportLoginBase64.get().getSecret_key();
//				String md5OurSecretKey = MD5Utils.generateKey(InitServlet.ESPORT_SECRET_TOKEN);
//				long timeStamp = esportLoginBase64.get().getTimeStamp();
//				if (System.currentTimeMillis() - timeStamp > 60000) {
//					logger.error("EsportVerifyApi Token has expired");
//					sendResponse(response, esportVerifyResponse);
//					return;
//				}
//				if (StringUtils.isBlank(ourSecretKey) || !md5OurSecretKey.equals(ourSecretKey)) {
//					logger.error("EsportVerifyApi Secret key blank or invalid after decode from token");
//					sendResponse(response, esportVerifyResponse);
//					return;
//				}
//				String loginName = esportLoginBase64.get().getLoginName();
//				String esportUserName = esportLoginBase64.get().getEsport_userName();
//				if (StringUtils.isBlank(loginName) || StringUtils.isBlank(esportUserName)) {
//					logger.error("EsportVerifyApi Login name or esportUserName is empty after decode from token");
//					sendResponse(response, esportVerifyResponse);
//					return;
//				}
//				//check exist esport by loginname
//				BaseResponse<String> resp = GamesCommonService.checkPlayerExist(loginName, "ESPORT");
//				if (resp != null && 0 == resp.getCode()) {
//					String md5esportUserName = MD5Utils.generateKey(resp.getData());
//					if (esportUserName.equals(md5esportUserName)) {
//						logger.info("EsportVerifyApi Handle request from ESport success , esport id =" + resp.getData());
//						esportVerifyResponse.setLoginName(resp.getData());
//						sendResponse(response, esportVerifyResponse);
//						return;
//					}
//				} else {
//					logger.error(resp.getMessage());
//					logger.error("EsportVerifyApi There's no sports book user item match with loginName after decode");
//					sendResponse(response, esportVerifyResponse);
//					return;
//				}
//				
//			} else {
//				logger.error("EsportVerifyApi Cannot decode token");
//				sendResponse(response, esportVerifyResponse);
//				return;
//			}
//
//		} catch (Exception ex) {
//			ex.printStackTrace();
//			logger.error("EsportVerifyApi Has an exception when handle request from Esport");
//			sendResponse(response, esportVerifyResponse);
//		}
//		long endTime = System.nanoTime();
//		String message = String.format("Time to complete doPost: %s seconds",
//				CommonMethod.DEC_FORMATTER.format((endTime - startTime) / 1e9));
//		logger.info(message);
//	}
//
//	private void sendResponse(HttpServletResponse response,
//			EsportVerifyResponse EsportVerifyResponse) throws IOException {
//		ObjectMapper Obj = new ObjectMapper();
//		String responseData = Obj.writeValueAsString(EsportVerifyResponse);
//		CommonMethod.printData(response, responseData);
//	}
//
//}