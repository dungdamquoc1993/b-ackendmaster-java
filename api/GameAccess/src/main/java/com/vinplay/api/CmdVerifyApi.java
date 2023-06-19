///**
// * Archie
// */
//package com.vinplay.api;
//
//import java.io.IOException;
//import java.io.StringWriter;
//import java.util.Optional;
//
//import javax.servlet.ServletException;
//import javax.servlet.http.HttpServlet;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import javax.xml.bind.JAXBContext;
//import javax.xml.bind.JAXBException;
//import javax.xml.bind.Marshaller;
//
//import org.apache.commons.lang.StringUtils;
//import org.apache.log4j.Logger;
//
//import com.vinplay.dao.cmd.CmdDao;
//import com.vinplay.dao.cmd.impl.CmdDaoImpl;
//import com.vinplay.dto.sportsbook.SportsbookLoginBase64;
//import com.vinplay.dto.sportsbook.SportsbookLoginEntryXMLResponse;
//import com.vinplay.interfaces.sportsbook.SportsbookAllServices;
//import com.vinplay.item.SportsbookItem;
//import com.vinplay.logic.CommonMethod;
//import com.vinplay.usercore.utils.GameThirdPartyInit;
//import com.vinplay.utils.GlobalConstants;
//import com.vinplay.utils.HttpUtils;
//import com.vinplay.utils.MD5Utils;
//
///**
// * @author Archie
// *
// */
////@WebServlet(name = "CmdVerifyApi", description = "API for verify request from CMD", urlPatterns = { "/api/cmd/verify" })
//public class CmdVerifyApi extends HttpServlet {
//
//	private static final long serialVersionUID = 1L;
//	private static final Logger logger = Logger.getLogger(CmdVerifyApi.class);
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
//		SportsbookLoginEntryXMLResponse sportsbookLoginEntryXMLResponse = new SportsbookLoginEntryXMLResponse();
//		sportsbookLoginEntryXMLResponse.setMember_id("");
//		sportsbookLoginEntryXMLResponse.setMessage("failed");
//		sportsbookLoginEntryXMLResponse.setStatus_code(400);
//		// check IP
//		String ipAddress = HttpUtils.getIpAddress(request);
//		if("dev".equals(GameThirdPartyInit.enviroment)) {
////			if (!GlobalConstants.IP_CMD_STAGING.contains(ipAddress)) {
////				logger.error("Please check your IP Address again ,ipaddress = " + ipAddress);
////				sportsbookLoginEntryXMLResponse.setMessage("Please check your IP Address again ,ipaddress = " + ipAddress);
////				sendResponse(response, sportsbookLoginEntryXMLResponse);
////				return;
////			}
//		}else {
//			if (!GlobalConstants.IP_CMD_PRO.contains(ipAddress)) {
//				logger.error("Please check your IP Address again ,ipaddress = " + ipAddress);
//				sportsbookLoginEntryXMLResponse.setMessage("Please check your IP Address again ,ipaddress = " + ipAddress);
//				sendResponse(response, sportsbookLoginEntryXMLResponse);
//				return;
//			}
//		}
//		
//		long startTime = System.nanoTime();
//		String token = request.getParameter("token");
//		logger.info("[CmdVerifyApi request params] : " + token);
//		try {
//			if (StringUtils.isBlank(token)) {
//				logger.error("CmdVerifyApi Params is null or empty");
//				sportsbookLoginEntryXMLResponse.setMessage("CmdVerifyApi Params is null or empty");
//				sendResponse(response, sportsbookLoginEntryXMLResponse);
//				return;
//			}
//			
//			Optional<SportsbookLoginBase64> sportsBookLoginBase64 = SportsbookAllServices.getInstance()
//					.sportsbookLoginDecode(token);
//			if (sportsBookLoginBase64.isPresent()) {
//				String ourSecretKey = sportsBookLoginBase64.get().getSecret_key();
//				String md5OurSecretKey = MD5Utils.generateKey(GameThirdPartyInit.SPORTS_BOOK_OUR_SECRET_KEY);
//				long timeStamp = sportsBookLoginBase64.get().getTimeStamp();
//				if (System.currentTimeMillis() - timeStamp > 60000) {
//					logger.error("CmdVerifyApi Token has expired");
//					sportsbookLoginEntryXMLResponse.setMessage("Token has expired");
//					sendResponse(response, sportsbookLoginEntryXMLResponse);
//					return;
//				}
//				if (StringUtils.isBlank(ourSecretKey) || !md5OurSecretKey.equals(ourSecretKey)) {
//					logger.error("CmdVerifyApi Secret key blank or invalid after decode from token");
//					sportsbookLoginEntryXMLResponse.setMessage("Token is invalid");
//					sendResponse(response, sportsbookLoginEntryXMLResponse);
//					return;
//				}
//				String loginName = sportsBookLoginBase64.get().getLoginName();
//				String sportsBookUserName = sportsBookLoginBase64.get().getSportsbook_userName();
//				if (StringUtils.isBlank(loginName) || StringUtils.isBlank(sportsBookUserName)) {
//					logger.error("CmdVerifyApi Login name or sportsBookUserName is empty after decode from token");
//					sportsbookLoginEntryXMLResponse.setMessage("Token is invalid");
//					sendResponse(response, sportsbookLoginEntryXMLResponse);
//					return;
//				}
//				CmdDao cmdDao =new CmdDaoImpl();
//				SportsbookItem sportsBookItem = cmdDao.findUserByNickName(loginName);
//				if (sportsBookItem!=null) {
//					String md5SportsBookUserName = MD5Utils.generateKey(sportsBookItem.getSportsbook_username());
//					if (sportsBookUserName.equals(md5SportsBookUserName)
//							&& loginName.equals(sportsBookItem.getLoginname())) {
//						logger.info("CmdVerifyApi Handle request from Sports book success");
//						sportsbookLoginEntryXMLResponse.setMember_id(sportsBookItem.getSportsbookid());
//						sportsbookLoginEntryXMLResponse.setMessage("Success");
//						sportsbookLoginEntryXMLResponse.setStatus_code(0);
//						sendResponse(response, sportsbookLoginEntryXMLResponse);
//						return;
//					}
//				} else {
//					logger.error("CmdVerifyApi There's no sports book user item match with loginName after decode");
//					sportsbookLoginEntryXMLResponse.setMessage("Token is invalid");
//					sendResponse(response, sportsbookLoginEntryXMLResponse);
//					return;
//				}
//			} else {
//				logger.error("CmdVerifyApi Cannot decode token");
//				sportsbookLoginEntryXMLResponse.setMessage("Token is invalid");
//				sendResponse(response, sportsbookLoginEntryXMLResponse);
//				return;
//			}
//
//		} catch (Exception ex) {
//			ex.printStackTrace();
//			logger.error("CmdVerifyApi Has an exception when handle request from Sports book");
//			sportsbookLoginEntryXMLResponse.setStatus_code(500);
//			sportsbookLoginEntryXMLResponse.setMessage("Exception when handle request");
//			sendResponse(response, sportsbookLoginEntryXMLResponse);
//		}
//		long endTime = System.nanoTime();
//		String message = String.format("Time to complete doPost: %s seconds",
//				CommonMethod.DEC_FORMATTER.format((endTime - startTime) / 1e9));
//		logger.info(message);
//	}
//
//	private void sendResponse(HttpServletResponse response,
//			SportsbookLoginEntryXMLResponse sportsbookLoginEntryXMLResponse) throws IOException {
//		String xmlResponse = convertXMLObjectToString(sportsbookLoginEntryXMLResponse);
//		CommonMethod.printData(response, xmlResponse);
//	}
//
//	private String convertXMLObjectToString(SportsbookLoginEntryXMLResponse sportsbookLoginEntryXMLResponse) {
//		try {
//			JAXBContext jaxbContext = JAXBContext.newInstance(sportsbookLoginEntryXMLResponse.getClass());
//			Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
//			jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
//			StringWriter sw = new StringWriter();
//			jaxbMarshaller.marshal(sportsbookLoginEntryXMLResponse, sw);
//			return sw.toString();
//		} catch (JAXBException e) {
//			logger.error("");
//			return "";
//		}
//	}
//}