//package com.vinplay.logic;
//
//import org.apache.log4j.Logger;
//
//import javax.servlet.ServletException;
//import javax.servlet.http.HttpServlet;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import java.io.IOException;
//import java.io.InputStream;
//
///**
// * DELETE
// *
// * @author Administrator
// */
//public class BackService extends HttpServlet {
//    private static Logger logger = Logger.getLogger(BackService.class);
//    private static final long serialVersionUID = 1L;
//
//    @Override
//    protected void doGet(HttpServletRequest req, HttpServletResponse res)
//            throws ServletException, IOException {
//        logger.info("LTServer BackService GET METHOD");
//    }
//
//    @Override
//    protected void doPost(HttpServletRequest request,
//                          HttpServletResponse response) throws ServletException, IOException {
//        InputStream in = request.getInputStream();
//        String strdata = CommonMethod.inputStream2String(in);
//        logger.info("LTServer BackService POST ：" + strdata);
//
//        if (strdata == null)
//            return;
//        String[] args = strdata.split("&");
//        if (args.length < 1) {
//            return;
//        }
//        if ("initGame3rdConfig".equals(args[0])) {
//            InitData.initGame3rdConfig();
//        }
//    }
//
//}
