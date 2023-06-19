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
//import com.vinplay.service.esport.EsportService;
//import com.vinplay.service.esport.ipml.EsportServiceImpl;
//import com.vinplay.utils.HttpUtils;
//
///**
// * @author Archie
// *
// */
//@WebServlet(name = "EsportApi", description = "API for Esport games", urlPatterns = { "/api/esport" })
//public class EsportApi extends HttpServlet {
//	/**
//	 * 
//	 */
//	private static final long serialVersionUID = -7651078352307234600L;
//	private static final Logger logger = Logger.getLogger(EsportApi.class);
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
//			if (!InitData.isEsportDown()) {
//				type = objInStream.readObject().toString();
//				EsportService service = new EsportServiceImpl();
//				switch (type) {
//				case "FundTransferEsport": {
//					ResultFormat rf = service.transfer(objInStream);
//					oos.writeObject(rf.getRes());
//					oos.writeObject(rf.getList());
//					oos.writeObject(rf.getMsg());
//					break;
//				}
//				case "CheckBalanceEsport": {
//					String loginname = objInStream.readObject().toString();
//					ResultFormat rf = service.getBalance(loginname,true);
//					oos.writeObject(Integer.valueOf(rf.getRes()));
//					oos.writeObject(rf.getList());
//					oos.writeObject(rf.getMsg());
//					break;
//				}
//				case "LoginEsport": {
//					String loginname = objInStream.readObject().toString();
//					ResultFormat rf = service.login(loginname);
//					oos.writeObject(Integer.valueOf(rf.getRes()));
//					oos.writeObject(rf.getList());
//					oos.writeObject(rf.getMsg());
//					break;
//				}
//				default:
//					logger.error("Type is not exist , type = " + type);
//					oos.writeObject(1);
//					oos.writeObject("");
//					oos.writeObject("Type is not exist !");
//				}
//			} else {
//				logger.info("Esport maintained");
//				oos.writeObject(2);
//				oos.writeObject("");
//				oos.writeObject("Hệ thống đag bảo trì .Vui lòng quay lại sau ");
//			}
//
//			oos.flush();
//		} catch (Exception ex) {
//			oos.writeObject("1");
//			oos.writeObject(ex.getMessage());
//			oos.writeObject("");
//			logger.error("Abnormal interface！type=" + type, ex);
//		} finally {
//			if (in != null)
//				in.close();
//			if (objInStream != null)
//				objInStream.close();
//
//			if (outstream != null)
//				outstream.close();
//			if (oos != null)
//				oos.close();
//		}
//
//	}
//
//}