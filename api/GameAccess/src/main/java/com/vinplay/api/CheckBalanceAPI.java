///**
// * Archie
// */
//package com.vinplay.api;
//
///**
// * @author Archie
// *
// */
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
//import com.vinplay.interfaces.sportsbook.SportsbookMemberServices;
//import com.vinplay.logic.CommonMethod;
//import com.vinplay.service.ag.AgService;
//import com.vinplay.service.ag.impl.AgServiceImpl;
//import com.vinplay.service.ebet.EbetCommonService;
//import com.vinplay.service.esport.EsportService;
//import com.vinplay.service.esport.ipml.EsportServiceImpl;
//import com.vinplay.service.sa.SaGame3rdService;
//import com.vinplay.service.sbo.SboUserService;
//import com.vinplay.service.sbo.impl.SboUserServiceImpl;
//import com.vinplay.service.sexygame.SGService;
//import com.vinplay.service.wm.WMService;
//import com.vinplay.utils.HttpUtils;
//
///**
// * @author Archie
// *
// */
//@WebServlet(name = "CheckBalanceAPI", description = "API for checking games balances", urlPatterns = { "/api/checkbalances" })
//public class CheckBalanceAPI extends HttpServlet {
//	/**
//	 * 
//	 */
//	private static final long serialVersionUID = 1096765603954791382L;
//	/**
//	 * 
//	 */
//	private static final Logger logger = Logger.getLogger(CheckBalanceAPI.class);
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
//		
//		try {
//			String loginname = (String) objInStream.readObject();
//            String gameType = (String) objInStream.readObject();
//            if(loginname ==null || "".equals(loginname)) {
//            	logger.error("loginname is null or empty ");
//				oos.writeObject(1);
//                oos.writeObject("loginname is null or empty");
//            }
//            
//            if(gameType ==null || "".equals(gameType)) {
//            	logger.error("gameType is null or empty ");
//				oos.writeObject(1);
//                oos.writeObject("gameType is null or empty ");
//            }
//            
//            ResultFormat rf = null;
//			switch (gameType) {
//			case "SA":
//				rf = SaGame3rdService.getUserInfo(loginname, gameType);
//				break;
//			case "EBET":
//				rf = EbetCommonService.getUserInfo(loginname);
//				break;
//			case "AG":
//				AgService dao =new AgServiceImpl();
//			    rf = dao.getUserInfo(loginname);
//				break;
//			case "IBC2":
//				break;
//			case "SBO":
//				SboUserService service =new SboUserServiceImpl();
//				rf = service.getUserInfo(loginname, gameType);
//				break;
//			case "WM":
//				rf = WMService.CheckUserBalanceByLoginname(loginname);
//				break;	
//			case "CMD":
//				rf = SportsbookMemberServices.getInstance().getUserInfo(loginname, gameType);
//				break;
//			case "ESPORT":
//				EsportService eService = new EsportServiceImpl();
//				rf = eService.getBalance(loginname, false);
//				break;
//			case "SG":
//				rf = SGService.QueryPlayer(loginname);
//				break;
//			default:
//				break;
//			}
//            oos.writeObject(Integer.valueOf(rf.getRes()));
//            oos.writeObject(rf.getList());
//            oos.writeObject(rf.getMsg());
//			
//			oos.flush();
//		} catch (Exception ex) {
//			oos.writeObject("1");
//			oos.writeObject(ex.getMessage());
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
