///**
// * Archie
// */
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
//import com.vinplay.service.sa.SACommonService;
//import com.vinplay.service.sa.SaGame3rdService;
//import com.vinplay.service.sa.SaGameAccessService;
//import com.vinplay.utils.HttpUtils;
//
///**
// * @author Archie
// *
// */
//@WebServlet(name = "SAGamesApi", description = "API for SA games", urlPatterns = { "/api/sa" })
//public class SAGamesApi extends HttpServlet {
//	/**
//	 * 
//	 */
//	private static final long serialVersionUID = 291452581316492077L;
//	private static final Logger logger = Logger.getLogger(SAGamesApi.class);
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
//		InputStream in = request.getInputStream();
//		ObjectInputStream objInStream = new ObjectInputStream(in);
//		OutputStream outstream = response.getOutputStream();
//		ObjectOutputStream oos = new ObjectOutputStream(outstream);
//		String type = null;
//		try {
//			if (!InitData.isSADown()) {
//				type = objInStream.readObject().toString();
//				switch (type) {
//				case "SA_LoginOrCreatePlayer": {//frontend
//                    ResultFormat rf = SaGameAccessService.LoginOrCreatePlayer(objInStream);
//                    oos.writeObject(Integer.valueOf(rf.getRes()));
//                    oos.writeObject(rf.getList());
//                    oos.writeObject(rf.getMsg());
//                    break;
//                }
//                case "SA_GetPlayerInfo": {//frontend
//                    ResultFormat rf = SaGameAccessService.SA_GetPlayerInfo(objInStream);
//                    oos.writeObject(Integer.valueOf(rf.getRes()));
//                    oos.writeObject(rf.getList());
//                    oos.writeObject(rf.getMsg());
//                    break;
//                }
//                case "SA_DepositPlayerMoney": {//frontend, MG
//                    ResultFormat rf = SACommonService.SADepositPlayerMoney(objInStream);
//                    oos.writeObject(Integer.valueOf(rf.getRes()));
//                    oos.writeObject(rf.getList());
//                    oos.writeObject(rf.getMsg());
//                    break;
//                }
//                case "SA_WithdrawPlayerMoney": {//frontend, MG
//                    ResultFormat rf = SACommonService.WithdrawPlayerMoney(objInStream);
//                    oos.writeObject(Integer.valueOf(rf.getRes()));
//                    oos.writeObject(rf.getList());
//                    oos.writeObject(rf.getMsg());
//                    break;
//                }
//                case "SA_QueryPlayer": {//from MG
//                    ResultFormat rf = SaGame3rdService.QueryPlayer(objInStream);
//                    oos.writeObject(Integer.valueOf(rf.getRes()));
//                    oos.writeObject(rf.getList());
//                    oos.writeObject(rf.getMsg());
//                    break;
//                }
//				default :
//					logger.error("Type is not exist , type = " + type);
//					oos.writeObject(1);
//                    oos.writeObject("Type is not exist !");
//				}
//			} else {
//				logger.info("SA Live Casino maintained");
//				oos.writeObject(2);
//                oos.writeObject("SA Live Casino  maintained !");
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