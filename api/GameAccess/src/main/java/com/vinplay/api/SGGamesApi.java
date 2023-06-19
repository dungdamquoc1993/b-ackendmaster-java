//package com.vinplay.api;
//
//import java.io.IOException;
//import java.io.InputStream;
//import java.io.ObjectInputStream;
//import java.io.ObjectOutputStream;
//import java.io.OutputStream;
//
//import javax.servlet.ServletException;
//import javax.servlet.annotation.WebServlet;
//import javax.servlet.http.HttpServlet;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//
//import org.apache.log4j.Logger;
//
//import com.vinplay.dto.ResultFormat;
//import com.vinplay.logic.CommonMethod;
//import com.vinplay.logic.InitData;
//import com.vinplay.service.sexygame.SGService;
//import com.vinplay.utils.HttpUtils;
//
//@WebServlet(name = "SGGamesApi", description = "API for Sexy games", urlPatterns = { "/api/sg" })
//public class SGGamesApi extends HttpServlet {
//	
//	/**
//	 * 
//	 */
//	private static final long serialVersionUID = 826626753733602544L;
//	private static final Logger logger = Logger.getLogger(SGGamesApi.class);
//
//	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//		doPost(request, response);
//	}
//
//	public void doPost(HttpServletRequest request, HttpServletResponse response)
//			throws ServletException, java.io.IOException {
//		// check IP
//		String ipAddress = HttpUtils.getIpAddress(request);
//		if (!CommonMethod.isValidIpAddress(ipAddress)) {
//			return;
//		}
//		// check type
//		InputStream in = request.getInputStream();
//		ObjectInputStream objInStream = new ObjectInputStream(in);
//		OutputStream outstream = response.getOutputStream();
//		ObjectOutputStream oos = new ObjectOutputStream(outstream);
//		String type = null;
//		
//		try {
//			if (!InitData.isSGDown()) {
//				type = objInStream.readObject().toString();
//				switch (type) {
////				case "SMgBetTDAynlse": {//from MG
////                    ResultFormat rf = AdminSearch.SMgBetTDAynlse(objInStream);
////                    resListReturn(oos, rf);
////                    break;
////                }
//                case "SG_LoginOrCreatePlayer": {//frontend
//                	ResultFormat rf = SGService.loginOrCreatePlayer(objInStream);
//                    oos.writeObject(Integer.valueOf(rf.getRes()));
//                    oos.writeObject(rf.getList());
//                    oos.writeObject(rf.getMsg());
//                    break;
//                }
//                
//                case "SG_GetBalanceByLoginName": {
//                	ResultFormat rf = SGService.getBalanceByLoginName(objInStream);
//                    oos.writeObject(Integer.valueOf(rf.getRes()));
//                    oos.writeObject(rf.getList());
//                    oos.writeObject(rf.getMsg());
//                    break;
//                }
//                case "SG_DepositPlayerMoney": {
//	            	ResultFormat rf = SGService.depositPlayerMoney(objInStream);
//	                oos.writeObject(Integer.valueOf(rf.getRes()));
//	                oos.writeObject(rf.getList());
//	                oos.writeObject(rf.getMsg());
//	                break;
//	            }
//                case "SG_WithdrawPlayerMoney": {
//	            	ResultFormat rf = SGService.withdrawPlayerMoney(objInStream);
//	                oos.writeObject(Integer.valueOf(rf.getRes()));
//	                oos.writeObject(rf.getList());
//	                oos.writeObject(rf.getMsg());
//	                break;
//                }
//				default :
//					logger.error("Type is not exist , type = " + type);
//					oos.writeObject(1);
//					oos.writeObject("Type is not exist !");
//				}
//			} else {
//				logger.info("SG maintained");
//				oos.writeObject(2);
//                oos.writeObject("SG maintained !");
//			}
//			
//			oos.flush();
//		} catch (Exception ex) {
//			oos.writeObject("1");
//			oos.writeObject(ex.getMessage());
//			logger.error("Abnormal interface！type=" + type, ex);
//		}finally {
//			if (in != null) in.close();
//            if (objInStream != null) objInStream.close();
//
//            if (outstream != null) outstream.close();
//            if (oos != null) oos.close();
//		}
//		
//	}
//	
//}