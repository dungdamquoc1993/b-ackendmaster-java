package com.vinplay.pay.server;

import java.io.*;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.stream.Collectors;

import javax.servlet.DispatcherType;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import com.hazelcast.core.HazelcastInstance;
import com.vinplay.dal.service.impl.TaiXiuServiceImpl;
import com.vinplay.dichvuthe.service.impl.RechargeServiceImpl;
import com.vinplay.livecasino.api.core.obj.BalanceResponse;
import com.vinplay.livecasino.api.core.obj.LaunchGameResponse;
import com.vinplay.livecasino.api.core.obj.TCGBaseResponse;
import com.vinplay.livecasino.api.core.obj.TCGamingConfigObj;
import com.vinplay.livecasino.api.wsclient.TCGamingAPICommon;
import com.vinplay.livecasino.api.wsclient.impl.TCGamingAPICommonImpl;
import com.vinplay.usercore.utils.PartnerConfig;
import com.vinplay.utils.TelegramAlert;
import com.vinplay.vbee.common.hazelcast.HazelcastClientFactory;
import com.vinplay.vbee.common.models.cache.UserCacheModel;
import com.vinplay.vbee.common.models.minigame.TopWin;
import com.vinplay.vbee.common.utils.VinPlayUtils;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.apache.commons.fileupload.FileItemIterator;
import org.apache.commons.fileupload.FileItemStream;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.fileupload.util.Streams;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.HttpConfiguration;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.server.handler.HandlerCollection;
import org.eclipse.jetty.server.handler.IPAccessHandler;
import org.eclipse.jetty.servlet.ServletHandler;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.vinplay.api.otp.PayResponse;
import com.vinplay.dichvuthe.response.RechargePaywellResponse;
import com.vinplay.payment.entities.PaywellNotifyRequest;
import com.vinplay.payment.service.RechargeOneClickPayService;
import com.vinplay.payment.service.RechargePayWellService;
import com.vinplay.payment.service.RechargePayaSecService;
import com.vinplay.payment.service.RechargePrincePayService;
import com.vinplay.payment.service.WithDrawPrincePayService;
import com.vinplay.payment.service.impl.RechargeOneClickPayServiceImpl;
import com.vinplay.payment.service.impl.RechargePayWellServiceImpl;
import com.vinplay.payment.service.impl.RechargePayaSecServiceImpl;
import com.vinplay.payment.service.impl.RechargePrincePayServiceImpl;
import com.vinplay.payment.service.impl.WithDrawPrincePayServiceImpl;
import com.vinplay.payment.utils.Constant;
import com.vinplay.payment.utils.PayUtils;
import com.vinplay.usercore.utils.GameCommon;
import com.vinplay.usercore.utils.GameThirdPartyInit;
import com.vinplay.vbee.common.config.VBeePath;
import com.vinplay.vbee.common.cp.BaseController;
import com.vinplay.vbee.common.hazelcast.HazelcastLoader;
import com.vinplay.vbee.common.mongodb.MongoDBConnectionFactory;
import com.vinplay.vbee.common.response.BaseResponse;
import com.vinplay.vbee.common.rmq.RMQApi;
import com.vinplay.vbee.common.statics.Consts;

public class JettyServer {
    private static final Logger logger = Logger.getLogger(JettyServer.class);
    private static final Logger blackListIpLogger = Logger.getLogger("BlackListIpLogger");
    private static final String LOG_PROPERTIES_FILE = "config/log4j.properties";
    private static int HTTP_PORT = 8081;
    private static int SSL_PORT = 443;
    private static long IDLE_TIMEOUT = 30000L;
    private static BaseController<HttpServletRequest, String> controller;
    private static String basePasth;

    private static void initializeLogger() {
        Properties logProperties = new Properties();
        try {
            File file = new File(basePasth.concat(LOG_PROPERTIES_FILE));
            logProperties.load(new FileInputStream(file));
            PropertyConfigurator.configure((Properties) logProperties);
            logger.info("Logging initialized.");
        } catch (IOException e) {
            throw new RuntimeException("Unable to load logging property config/log4j.properties");
        }
    }

    public static String getIpAddress(HttpServletRequest request) {
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

    public static class PayPrinceNotifyServlet extends HttpServlet {
        private static final long serialVersionUID = 1L;

        protected void doGet(HttpServletRequest request, HttpServletResponse response)
                throws ServletException, IOException {
            doPost(request, response);
        }

        protected void doPost(HttpServletRequest request, HttpServletResponse response)
                throws ServletException, IOException {
            this.onExecute(request, response);
        }

        private void onExecute(HttpServletRequest request, HttpServletResponse response) throws IOException {
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.setStatus(200);
            String status = request.getParameter("status");
            String result = request.getParameter("result");
            String sign = request.getParameter("sign");
            logger.info("Notify princepay , status = " + status + ",result=" + result + ",sign=" + sign);
            RechargePrincePayService service = new RechargePrincePayServiceImpl();
            //String requestData = request.getReader().lines().collect(Collectors.joining());
            int statusInt = 0;
            try {
                statusInt = Integer.parseInt(status);
            } catch (NumberFormatException e) {
                logger.error(e);
                return;
            }
            if (StringUtils.isBlank(result) || StringUtils.isBlank(sign))
                return;

            RechargePaywellResponse responseData = service.notify(statusInt, result, sign);
            logger.info("Notify princepay , response = " + responseData.toJson());
            response.getWriter().println(responseData.toJson());
        }
    }

    public static class PayPrinceWithDrawNotifyServlet extends HttpServlet {
        private static final long serialVersionUID = 1L;

        protected void doGet(HttpServletRequest request, HttpServletResponse response)
                throws ServletException, IOException {
            doPost(request, response);
        }

        protected void doPost(HttpServletRequest request, HttpServletResponse response)
                throws ServletException, IOException {
            this.onExecute(request, response);
        }

        private void onExecute(HttpServletRequest request, HttpServletResponse response) throws IOException {
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.setStatus(200);
            String status = request.getParameter("status");
            String result = request.getParameter("result");
            String sign = request.getParameter("sign");
            logger.info("Notify princepay , status = " + status + ",result=" + result + ",sign=" + sign);
            WithDrawPrincePayService service = new WithDrawPrincePayServiceImpl();
            //String requestData = request.getReader().lines().collect(Collectors.joining());
            int statusInt = 0;
            try {
                statusInt = Integer.parseInt(status);
            } catch (NumberFormatException e) {
                logger.error(e);
                return;
            }
            if (StringUtils.isBlank(result) || StringUtils.isBlank(sign))
                return;

            boolean responseData = service.notify(statusInt, result, sign);
            if (responseData) {
                response.getWriter().println("{\"get\":\"success\"}");
            } else {
                response.getWriter().println("{\"get\":\"false\"}");
            }
        }
    }

//	public static class PayPrinceWithDrawClickServlet extends HttpServlet {
//		private static final long serialVersionUID = 1L;
//
//		protected void doGet(HttpServletRequest request, HttpServletResponse response)
//				throws ServletException, IOException {
//			doPost(request, response);
//		}
//
//		protected void doPost(HttpServletRequest request, HttpServletResponse response)
//				throws ServletException, IOException {
//			this.onExecute(request, response);
//		}
//
//		private void onExecute(HttpServletRequest request, HttpServletResponse response) throws IOException {
//			response.setContentType("application/json");
//			response.setCharacterEncoding("UTF-8");
//			response.setStatus(200);
//			String c = request.getParameter("c");
//			String ip =  "";
//			try{
//				ip = request.getParameter("ip");
//			}catch (Exception e) { }
//
//			RechargePaywellResponse responseData = new RechargePaywellResponse(1, 0L, 0, 0L,"");
//			WithDrawPrincePayService service = new WithDrawPrincePayServiceImpl();
//			if(c.contains("0")) {
//				String userId =  request.getParameter("userId");
//				String username = request.getParameter("username");
//				String nickname = request.getParameter("nickname");
//				String bankNumber = request.getParameter("bankNumber");
//				long amount = Long.valueOf(request.getParameter("amount"));
//				logger.info("Withdraw oneclick , userId = " + userId + ",username=" + username + ",nickname=" + nickname
//						+ ",amount=" + String.valueOf(amount) + ",bankNumber=" + bankNumber);
//				responseData = service.requestWithdrawUser(userId, username, nickname, amount, bankNumber);
//			}
//
//			if(c.contains("1")) {
//				String orderId = request.getParameter("orderId");
//				String channel = request.getParameter("channel");
//				String staffUserName = request.getParameter("approvedName");
//				logger.info("Withdraw oneclick by staff , orderId = " + orderId + ",channel=" + channel + ",staffUserName=" + staffUserName
//						+ ",ip=" + ip);
//				responseData = service.withdrawal(orderId, channel, staffUserName, ip);
//			}
//			response.getWriter().println(responseData.toJson());
//		}
//	}
//	


    public static class PayOneClickDepositNotifyServlet extends HttpServlet {
        private static final long serialVersionUID = 1L;

        protected void doGet(HttpServletRequest request, HttpServletResponse response)
                throws ServletException, IOException {
            doPost(request, response);
        }

        protected void doPost(HttpServletRequest request, HttpServletResponse response)
                throws ServletException, IOException {
            this.onExecute(request, response);
        }
        //

        private void onExecute(HttpServletRequest request, HttpServletResponse response) throws IOException {
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.setStatus(200);
            String remoteAddr = getIpAddress(request);
            if (!"52.77.84.74".equalsIgnoreCase(remoteAddr)) {
                logger.error("Remote IP Address PayOneClickDepositNotifyServlet = " + remoteAddr);
                return;
            }


            String amount = request.getParameter("amount");
            String net_amount = request.getParameter("net_amount");
            String transactionId = request.getParameter("tnx");
            String orderId = request.getParameter("merchant_txn");
            String sign = request.getParameter("sign");
            logger.info("Notify oneclick , amount = " + amount + ",net_amount=" + net_amount + ",transactionId="
                    + transactionId + ",orderId=" + orderId + ",sign=" + sign);
            RechargeOneClickPayService service = new RechargeOneClickPayServiceImpl();
//			RechargePaywellResponse responseData = service.notify(statusInt, result,sign);
            RechargePaywellResponse responseData = service.notify(amount, net_amount, transactionId, orderId, sign);
            if (responseData.getCode() == 0) {
                response.getWriter().println("VERIFIED");
            } else {
                response.getWriter().println("FAILED");
            }
        }
    }

    public static class PayWellNotifyServlet extends HttpServlet {
        private static final long serialVersionUID = 1L;
        private static final List<String> IP_PAYWELL = Arrays.asList(new String[]{"127.0.0.1", "0:0:0:0:0:0:0:1"});

        protected void doGet(HttpServletRequest request, HttpServletResponse response)
                throws ServletException, IOException {
            doPost(request, response);
        }

        protected void doPost(HttpServletRequest request, HttpServletResponse response)
                throws ServletException, IOException {
            this.onExecute(request, response);
        }

        private void onExecute(HttpServletRequest request, HttpServletResponse response) throws IOException {
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.setStatus(200);

            String remoteAddr = getIpAddress(request);
            if (!IP_PAYWELL.contains(remoteAddr)) {
                logger.error("Remote IP Address IP_PAYWELL_NOTALLOW = " + remoteAddr);
                return;
            }

            logger.info("Remote IP Address PAYWELL " + remoteAddr);
            PaywellNotifyRequest requestObj = new PaywellNotifyRequest();
            boolean isMultipart = ServletFileUpload.isMultipartContent(request);
            if (isMultipart) {
                ServletFileUpload upload = new ServletFileUpload();
                try {
                    FileItemIterator iter = upload.getItemIterator(request);
                    InputStream stream = null;
                    while (iter.hasNext()) {
                        FileItemStream item = iter.next();
                        String name = item.getFieldName().toLowerCase();
                        try {
                            stream = item.openStream();
                            if (item.isFormField()) {
                                String value = Streams.asString(stream);
                                switch (name) {
                                    case "merchantcode":
                                        requestObj.setMerchantCode(value);
                                        break;
                                    case "cartid":
                                        requestObj.setCartId(value);
                                        break;
                                    case "referenceid":
                                        requestObj.setReferenceId(value);
                                        break;
                                    case "amount":
                                        requestObj.setAmount(Double.parseDouble(value));
                                        break;
                                    case "amountfee":
                                        requestObj.setAmountFee(Double.parseDouble(value));
                                        break;
                                    case "currencycode":
                                        requestObj.setCurrencyCode(value);
                                        break;
                                    case "status":
                                        requestObj.setStatus(Integer.parseInt(value));
                                        break;
                                    case "requesttime":
                                        requestObj.setRequestTime(Long.parseLong(value));
                                        break;
                                    case "signature":
                                        requestObj.setSignature(value);
                                        break;
                                    default:
                                        break;
                                }
                            }
                        } finally {
                            if (stream != null)
                                stream.close();
                        }
                    }
                } catch (FileUploadException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

            }
            String res = "";
            logger.info("Request notify paywell " + requestObj.toJson());
            try {
                RechargePayWellService service = new RechargePayWellServiceImpl();
                RechargePaywellResponse rechargeResponse = service.notification(requestObj);
                logger.info("Response notify paywell " + rechargeResponse.toJson());
                if (rechargeResponse.getCode() == 0) {
                    res = new PayResponse(1, "success").toJson();
                } else {
                    res = new PayResponse(0, rechargeResponse.getData()).toJson();
                }
            } catch (Exception e) {
                logger.error(e);
                res = new PayResponse(0, e.getMessage()).toJson();
            }
            response.getWriter().println(res);
        }
    }

    public static class OneClickServlet extends HttpServlet {
        private static final long serialVersionUID = 1L;

        protected void doGet(HttpServletRequest request, HttpServletResponse response)
                throws ServletException, IOException {
            doPost(request, response);
        }

        protected void doPost(HttpServletRequest request, HttpServletResponse response)
                throws ServletException, IOException {
            this.onExecute(request, response);
        }

        private void onExecute(HttpServletRequest request, HttpServletResponse response) throws IOException {
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.setStatus(200);
            String userId = request.getParameter("userId");
            String username = request.getParameter("username");
            String nickname = request.getParameter("nickname");
            String customerName = request.getParameter("customerName");
            String bankCode = request.getParameter("bankCode");
            String channel = request.getParameter("channel");
            String ip = request.getParameter("ip");
            String res = "";
            long amount = 0;
            try {
                amount = Long.parseLong(request.getParameter("amount"));
            } catch (Exception e) {
                res = "{\"code\":99,\"message\":\"" + e.getMessage() + "\"}";
            }
            try {
                RechargeOneClickPayService service = new RechargeOneClickPayServiceImpl();
                RechargePaywellResponse rechargeResponse = service.createTransaction(userId, username, nickname, amount, channel, customerName, bankCode, ip);
                if (rechargeResponse.getCode() == 0) {
                    res = "{\"code\":1,\"message\":\"success\"}";
                } else {
                    res = "{\"code\":0,\"message\":\"" + rechargeResponse.getData() + "\"}";
                }
            } catch (Exception e) {
                logger.error(e);
                res = BaseResponse.error(Constant.ERROR_SYSTEM, e.getMessage());
            }

            response.getWriter().println(res);
        }
    }

    public static class PayaSecNotifyServlet extends HttpServlet {
        private static final long serialVersionUID = 1L;
        private static final List<String> IP_PAYASEC = Arrays.asList(new String[]{"144.202.102.152"});

        protected void doGet(HttpServletRequest request, HttpServletResponse response)
                throws ServletException, IOException {
            doPost(request, response);
        }

        protected void doPost(HttpServletRequest request, HttpServletResponse response)
                throws ServletException, IOException {
            this.onExecute(request, response);
        }

        private void onExecute(HttpServletRequest request, HttpServletResponse response) throws IOException {
            try {
                String remoteAddr = getIpAddress(request);
                if (!IP_PAYASEC.contains(remoteAddr)) {
                    logger.error("Remote IP Address IP_PAYASEC_NOTALLOW = " + remoteAddr);
                    response.setStatus(403);
                    response.getWriter().println(BaseResponse.error(Constant.ERROR_SAMEIP, "Can not allow accept this api"));
                    return;
                }

                response.setContentType("application/json");
                response.setCharacterEncoding("UTF-8");
                response.setStatus(200);
                String body = request.getReader().lines().collect(Collectors.joining(System.lineSeparator()));
                Map<String, Object> result = new Gson().fromJson(
                        body, new TypeToken<HashMap<String, Object>>() {
                        }.getType()
                );
                String created = result.get("created") == null ? "" : result.get("created").toString();
                String updated = result.get("updated") == null ? "" : result.get("updated").toString();
                String refId = result.get("refId") == null ? "" : result.get("refId").toString();
                String refIdPartner = result.get("refIdPartner") == null ? "" : result.get("refIdPartner").toString();
                String gateway = result.get("gateway") == null ? "" : result.get("gateway").toString();
                String gatewayDetail = result.get("gatewayDetail") == null ? "" : result.get("gatewayDetail").toString();
                Double amount = Double.parseDouble(result.get("amount") == null ? "0" : result.get("amount").toString());
                Double fee = Double.parseDouble(result.get("fee") == null ? "0" : result.get("fee").toString());
                Double netAmount = Double.parseDouble(result.get("netAmount") == null ? "0" : result.get("netAmount").toString());
                Double status = Double.parseDouble(result.get("status") == null ? "0" : result.get("status").toString());
                String token = result.get("token") == null ? "" : result.get("token").toString();
                logger.info("Notify payasec" + body);
                if (StringUtils.isBlank(created)) {
                    response.setStatus(400);
                    response.getWriter().println(BaseResponse.error(Constant.ERROR_PARAM, "created can not empty"));
                }

                if (StringUtils.isBlank(updated)) {
                    response.setStatus(400);
                    response.getWriter().println(BaseResponse.error(Constant.ERROR_PARAM, "updated can not empty"));
                }

                if (StringUtils.isBlank(refId)) {
                    response.setStatus(400);
                    response.getWriter().println(BaseResponse.error(Constant.ERROR_PARAM, "refId can not empty"));
                }

                if (StringUtils.isBlank(refIdPartner)) {
                    response.setStatus(400);
                    response.getWriter().println(BaseResponse.error(Constant.ERROR_PARAM, "refIdPartner can not empty"));
                }

                if (StringUtils.isBlank(gateway)) {
                    response.setStatus(400);
                    response.getWriter().println(BaseResponse.error(Constant.ERROR_PARAM, "gateway can not empty"));
                }

                if (StringUtils.isBlank(gatewayDetail)) {
                    response.setStatus(400);
                    response.getWriter().println(BaseResponse.error(Constant.ERROR_PARAM, "gatewayDetail can not empty"));
                }

                if (amount == 0) {
                    response.setStatus(400);
                    response.getWriter().println(BaseResponse.error(Constant.ERROR_PARAM, "amount is invalid"));
                }

                if (netAmount == 0) {
                    response.setStatus(400);
                    response.getWriter().println(BaseResponse.error(Constant.ERROR_PARAM, "netAmount is invalid"));
                }

                if (StringUtils.isBlank(token)) {
                    response.setStatus(400);
                    response.getWriter().println(BaseResponse.error(Constant.ERROR_PARAM, "token can not empty"));
                }

                RechargePayaSecService service = new RechargePayaSecServiceImpl();
                RechargePaywellResponse responseData = service.notification(created, updated, refId,
                        refIdPartner, gateway, gatewayDetail, amount.longValue(), fee.longValue(),
                        netAmount.longValue(), status.intValue(), token);
                logger.info("Notify payasec , response = " + responseData.toJson());
                if (responseData.getCode() == 0) {
                    response.setStatus(200);
                    response.getWriter().println(responseData.toJson());
                    return;
                } else {
                    response.setStatus(400);
                    response.getWriter().println(BaseResponse.error(Constant.ERROR_PARAM, responseData.getData()));
                }
            } catch (Exception e) {
                // TODO: handle exception
                logger.error(e);
                response.setStatus(404);
                response.getWriter().println(BaseResponse.error(Constant.ERROR_SYSTEM, e.getMessage()));
            }
        }
    }

    public static void main(String[] args) {
        try {
            basePasth = VBeePath.initBasePath(JettyServer.class);
            JettyServer.initializeLogger();
            logger.info("STARTING WEBSERVICE PAY API .... !!!!");
            JettyServer.loadConfig();
            RMQApi.start(Consts.RMQ_CONFIG_FILE);
            // Config
            HazelcastLoader.start();
            //add game3rd config
//			GameThirdPartyInit.init();
            PartnerConfig.ReadConfig();
            MongoDBConnectionFactory.init();
            GameCommon.init();
            PayUtils.init();
            Server server = new Server();
            ServerConnector connector = new ServerConnector(server);
            connector.setPort(HTTP_PORT);
            connector.setIdleTimeout(IDLE_TIMEOUT);
            HttpConfiguration https = new HttpConfiguration();
            ServletHandler handler = new ServletHandler();
            handler.addFilterWithMapping(CorsFilter.class, "/*", EnumSet.of(DispatcherType.REQUEST));
            handler.addServletWithMapping(PayPrinceNotifyServlet.class, "/payprince/notify");
            handler.addServletWithMapping(PayPrinceWithDrawNotifyServlet.class, "/payprince/withdraw/notify");
            handler.addServletWithMapping(PayWellNotifyServlet.class, "/paywell/notify");
            handler.addServletWithMapping(PayaSecNotifyServlet.class, "/payasec/notify");
            handler.addServletWithMapping(Iwin99BuyCardServerlet.class, "/api/gachthe/callback");
            handler.addServletWithMapping(Iwin99MomoCallbackServlet.class, "/api/momo/callback");
            handler.addServletWithMapping(BankCallbackServlet.class, "/api/bank/callback");
            handler.addServletWithMapping(SendTelegramUserExistGo88Real.class, "/api/sendGoTele");
            handler.addServletWithMapping(GetTopTaiXiuServerlet.class, "/api/gettoptaixiu");
//			handler.addServletWithMapping(OneClickServlet.class, "/oneclick");
            //handler.addServletWithMapping(PayOneClickDepositNotifyServlet.class, "/oneclick/notify");
            //TODO

            IPAccessHandler ipAccessHandler = new IPAccessHandler();
            ipAccessHandler.setHandler(handler);
            HandlerCollection handlerCollection = new HandlerCollection();
            handlerCollection.setHandlers(new Handler[]{ipAccessHandler});
            server.setHandler(handlerCollection);
            server.addConnector(connector);
            server.start();
            logger.info("WEBSERVICE PAY API Started ...!!!");
//			ScheduleMain.run();
            server.join();

        } catch (Exception e) {
            logger.error(("WEBSERVICE PAY API Start error: " + e.getMessage()));
            e.printStackTrace();
        }
    }

    private static void loadConfig() throws Exception {
        File file = new File(basePasth.concat("config/api_portal.xml"));
        DocumentBuilderFactory dFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dFactory.newDocumentBuilder();
        org.w3c.dom.Document doc = dBuilder.parse(file);
        doc.getDocumentElement().normalize();
        NodeList nodeList = doc.getElementsByTagName("portal");
        Element el = (Element) nodeList.item(0);
        HTTP_PORT = Integer.parseInt(el.getElementsByTagName("http_port").item(0).getTextContent());
        SSL_PORT = Integer.parseInt(el.getElementsByTagName("ssl_port").item(0).getTextContent());
        IDLE_TIMEOUT = Integer.parseInt(el.getElementsByTagName("idle_timeout").item(0).getTextContent());
        Element cmds = (Element) el.getElementsByTagName("commands").item(0);
        NodeList cmdList = cmds.getElementsByTagName("command");
        HashMap<Integer, String> commandsMap = new HashMap<Integer, String>();
        for (int i = 0; i < cmdList.getLength(); ++i) {
            Element eCmd = (Element) cmdList.item(i);
            Integer id = Integer.parseInt(eCmd.getElementsByTagName("id").item(0).getTextContent());
            String path = eCmd.getElementsByTagName("path").item(0).getTextContent();
            logger.debug((id + " <-> " + path));
            System.out.println(id + " <-> " + path);
            commandsMap.put(id, path);
        }
        controller = new BaseController<>();
        controller.initCommands(commandsMap);
    }

    public static class Iwin99BuyCardServerlet
            extends HttpServlet {
        private static final long serialVersionUID = 1L;

        protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
            this.onExecute(request, response);
        }

        protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
            this.onExecute(request, response);
        }

        private void onExecute(HttpServletRequest request, HttpServletResponse response) throws IOException {
            logger.info("Iwin99BuyCardServerlet ...!!!");
            response.setContentType("text/html");
            response.setCharacterEncoding("UTF-8");
            response.setStatus(200);
            RechargeServiceImpl service = new RechargeServiceImpl();
            logger.info(request.getParameterMap());
            String result;
//            if (PartnerConfig.CongGachThe.trim().equals("HADONGPHO")) {
//                result = service.receiveResultFromHaDongPhoBuyCard(request.getParameterMap());
//            } else {
                result = service.receiveResultFromIwin99BuyCard(request.getParameterMap());
//            }

            response.getWriter().println(result);
        }
    }

    public static class Iwin99MomoCallbackServlet
            extends HttpServlet {
        private static final long serialVersionUID = 1L;

        protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
            this.onExecute(request, response);
        }

        protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
            this.onExecute(request, response);
        }

        private void onExecute(HttpServletRequest request, HttpServletResponse response) throws IOException {
            logger.info("Iwin99MomoCallbackServlet ...!!!");
            response.setContentType("text/html");
            response.setCharacterEncoding("UTF-8");
            response.setStatus(200);
            RechargeServiceImpl service = new RechargeServiceImpl();
            logger.info(request.getParameterMap());
            String result = service.rechargeByMomoIwin99(request.getParameterMap());
            response.getWriter().println(result);
        }
    }

    public static class HaDongPhoMomoCallbackServlet
            extends HttpServlet {
        private static final long serialVersionUID = 1L;

        protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
            this.onExecute(request, response);
        }

        protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
            this.onExecute(request, response);
        }

        private void onExecute(HttpServletRequest request, HttpServletResponse response) throws IOException {
            logger.info("Iwin99MomoCallbackServlet ...!!!");
            response.setContentType("text/html");
            response.setCharacterEncoding("UTF-8");
            response.setStatus(200);
            RechargeServiceImpl service = new RechargeServiceImpl();
            logger.info(request.getParameterMap());
            String payloadRequest = getBody(request);
            logger.debug("Iwin99MomoCallbackServlet : " + payloadRequest);
            String result = service.rechargeByMomoHaDongPho(payloadRequest);
            response.getWriter().println(result);
        }
    }


    public static String getBody(HttpServletRequest request) throws IOException {

        String body = null;
        StringBuilder stringBuilder = new StringBuilder();
        BufferedReader bufferedReader = null;

        try {
            InputStream inputStream = request.getInputStream();
            if (inputStream != null) {
                bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                char[] charBuffer = new char[128];
                int bytesRead = -1;
                while ((bytesRead = bufferedReader.read(charBuffer)) > 0) {
                    stringBuilder.append(charBuffer, 0, bytesRead);
                }
            } else {
                stringBuilder.append("");
            }
        } catch (IOException ex) {
            throw ex;
        } finally {
            if (bufferedReader != null) {
                try {
                    bufferedReader.close();
                } catch (IOException ex) {
                    throw ex;
                }
            }
        }

        body = stringBuilder.toString();
        return body;
    }

    public static class HaDongPhoBankCallbackServlet
            extends HttpServlet {
        private static final long serialVersionUID = 1L;

        protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
            this.onExecute(request, response);
        }

        protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
            this.onExecute(request, response);
        }

        private void onExecute(HttpServletRequest request, HttpServletResponse response) throws IOException {
            logger.info("Iwin99MomoCallbackServlet ...!!!");
            response.setContentType("text/html");
            response.setCharacterEncoding("UTF-8");
            response.setStatus(200);
            RechargeServiceImpl service = new RechargeServiceImpl();
            logger.info(request.getParameterMap());
            String result = service.rechargeByBankHaDongPho(request.getParameterMap());
            response.getWriter().println(result);
        }
    }

    public static class BankCallbackServlet
            extends HttpServlet {
        private static final long serialVersionUID = 1L;

        protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
            this.onExecute(request, response);
        }

        protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
            this.onExecute(request, response);
        }

        private void onExecute(HttpServletRequest request, HttpServletResponse response) throws IOException {
            logger.info("Iwin99MomoCallbackServlet ...!!!");
            response.setContentType("text/html");
            response.setCharacterEncoding("UTF-8");
            response.setStatus(200);
            RechargeServiceImpl service = new RechargeServiceImpl();
            logger.info(request.getParameterMap());
            String result = service.rechargeByBank(request.getParameterMap());
            response.getWriter().println(result);
        }
    }

    public static class SendTelegramUserExistGo88Real
            extends HttpServlet {

        protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
            this.onExecute(request, response);
        }

        protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
            this.onExecute(request, response);
        }

        private void onExecute(HttpServletRequest request, HttpServletResponse response) throws IOException {
            response.setContentType("text/html");
            response.setCharacterEncoding("UTF-8");
            response.setStatus(200);
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("status", "success");
                jsonObject.put("message", "Đã gửi thành công");
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
            response.getWriter().println(jsonObject.toString());

            StringBuilder parameter = new StringBuilder();
            HashMap<String, String> fields = new HashMap<>();
            Map<String, String[]> requestParameterMap = request.getParameterMap();
            requestParameterMap.entrySet().forEach(entry -> {
                String fieldName = entry.getKey();
                String fieldValue = entry.getValue()[0];
                parameter.append(fieldName).append(":").append(fieldValue).append(", ");
                if (fieldValue == null || fieldValue.length() <= 0) {
                    return;
                }
                fields.put(fieldName, fieldValue);
            });
            TelegramAlert.SendMessage(fields.get("userName") + "  .Có số dư: " + fields.get("balance"));

        }
    }

    public static class GetRankGo88
            extends HttpServlet {

        protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
            this.onExecute(request, response);
        }

        protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
            this.onExecute(request, response);
        }

        private void onExecute(HttpServletRequest request, HttpServletResponse response) throws IOException {
            response.setContentType("text/html");
            response.setCharacterEncoding("UTF-8");
            response.setStatus(200);

            StringBuilder parameter = new StringBuilder();
            HashMap<String, String> fields = new HashMap<>();
            Map<String, String[]> requestParameterMap = request.getParameterMap();
            requestParameterMap.entrySet().forEach(entry -> {
                String fieldName = entry.getKey();
                String fieldValue = entry.getValue()[0];
                parameter.append(fieldName).append(":").append(fieldValue).append(", ");
                if (fieldValue == null || fieldValue.length() <= 0) {
                    return;
                }
                fields.put(fieldName, fieldValue);
            });

            OkHttpClient client = new OkHttpClient().newBuilder()
                    .build();
            Request requestOk = new Request.Builder()
                    .url("https://go88.live/get-rank.html?t=" + fields.get("t"))
                    .method("GET", null)
                    .addHeader("x-csrf-token", "e5d7007bfe9868ecac9815c3c7b213a9")
                    .addHeader("authority", "go88.live")
                    .addHeader("accept", "application/json, text/javascript, */*; q=0.01")
                    .addHeader("accept-language", "vi,en;q=0.9,en-US;q=0.8,vi-VN;q=0.7,zh-CN;q=0.6,zh;q=0.5")
                    .addHeader("sec-ch-ua", "\".Not/A)Brand\";v=\"99\", \"Google Chrome\";v=\"103\", \"Chromium\";v=\"103\"")
                    .addHeader("sec-ch-ua-mobile", "?0")
                    .addHeader("sec-ch-ua-platform", "\"macOS\"")
                    .addHeader("sec-fetch-dest", "empty")
                    .addHeader("sec-fetch-mode", "cors")
                    .addHeader("sec-fetch-site", "same-origin")
                    .addHeader("user-agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/103.0.0.0 Safari/537.36")
                    .addHeader("x-requested-with", "XMLHttpRequest")
                    .build();
            Response responseOk = client.newCall(requestOk).execute();
            String data = responseOk.body().string();
            logger.info("GetNotificationGo88 ...!!! " + data);
            response.getWriter().println(data);
        }
    }

    public static class GetNotificationGo88
            extends HttpServlet {

        protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
            this.onExecute(request, response);
        }

        protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
            this.onExecute(request, response);
        }

        private void onExecute(HttpServletRequest request, HttpServletResponse response) throws IOException {
            response.setContentType("text/html");
            response.setCharacterEncoding("UTF-8");
            response.setStatus(200);

            StringBuilder parameter = new StringBuilder();
            HashMap<String, String> fields = new HashMap<>();
            Map<String, String[]> requestParameterMap = request.getParameterMap();
            requestParameterMap.entrySet().forEach(entry -> {
                String fieldName = entry.getKey();
                String fieldValue = entry.getValue()[0];
                parameter.append(fieldName).append(":").append(fieldValue).append(", ");
                if (fieldValue == null || fieldValue.length() <= 0) {
                    return;
                }
                fields.put(fieldName, fieldValue);
            });

            OkHttpClient client = new OkHttpClient().newBuilder()
                    .build();
            Request requestOk = new Request.Builder()
                    .url("https://go88.live/notifications.html?t=" + fields.get("t"))
                    .method("GET", null)
                    .addHeader("x-csrf-token", "e5d7007bfe9868ecac9815c3c7b213a9")
                    .addHeader("authority", "go88.live")
                    .addHeader("accept", "application/json, text/javascript, */*; q=0.01")
                    .addHeader("accept-language", "vi,en;q=0.9,en-US;q=0.8,vi-VN;q=0.7,zh-CN;q=0.6,zh;q=0.5")
                    .addHeader("sec-ch-ua", "\".Not/A)Brand\";v=\"99\", \"Google Chrome\";v=\"103\", \"Chromium\";v=\"103\"")
                    .addHeader("sec-ch-ua-mobile", "?0")
                    .addHeader("sec-ch-ua-platform", "\"macOS\"")
                    .addHeader("sec-fetch-dest", "empty")
                    .addHeader("sec-fetch-mode", "cors")
                    .addHeader("sec-fetch-site", "same-origin")
                    .addHeader("user-agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/103.0.0.0 Safari/537.36")
                    .addHeader("x-requested-with", "XMLHttpRequest")
                    .build();
            Response responseOk = client.newCall(requestOk).execute();
            String data = responseOk.body().string();
            logger.info("GetNotificationGo88 ...!!! " + data);
            response.getWriter().println();
        }
    }

    public static class TestRegisterUserCasino
            extends HttpServlet {

        protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
            this.onExecute(request, response);
        }

        protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
            this.onExecute(request, response);
        }

        private void onExecute(HttpServletRequest request, HttpServletResponse response) throws IOException {
            response.setContentType("text/html");
            response.setCharacterEncoding("UTF-8");
            response.setStatus(200);

            StringBuilder parameter = new StringBuilder();
            HashMap<String, String> fields = new HashMap<>();
            Map<String, String[]> requestParameterMap = request.getParameterMap();
            requestParameterMap.entrySet().forEach(entry -> {
                String fieldName = entry.getKey();
                String fieldValue = entry.getValue()[0];
                parameter.append(fieldName).append(":").append(fieldValue).append(", ");
                if (fieldValue == null || fieldValue.length() <= 0) {
                    return;
                }
                fields.put(fieldName, fieldValue);
            });

            TCGamingConfigObj configObj = new TCGamingConfigObj();
            configObj.setApiUrl("http://www.connect6play.com/doBusiness.do");
            configObj.setMerchantCode("go88vndk");
            configObj.setDesKey("ZU2JmW3M");
            configObj.setSha256Key("SLzDawl0l6At7m6f");
            String usernameReal = fields.get("usernamereal");
            String username = fields.get("username");
            String password = fields.get("password");
            String currency = "VND";

            logger.info("TestRegisterUserCasino ...!!! " + username + " " + password);
            HazelcastInstance client = HazelcastClientFactory.getInstance();
            TCGamingAPICommon api = new TCGamingAPICommonImpl(configObj);
            UserCacheModel user = (UserCacheModel) client.getMap("users").get(usernameReal);
            TCGBaseResponse responsedata = api.registerMember(username, password, currency);
            String id = VinPlayUtils.genTransactionId(user.getId());
            TCGBaseResponse responseDataFund = api.fundTransferIn(username, 112, 10, id);
            BalanceResponse responseDataBalance = api.getBalanceMember(username, 112);
            String id1 = VinPlayUtils.genTransactionId(user.getId());
            TCGBaseResponse responseDataFundOut = api.fundTransferOutAll(username, 112, id1);

            LaunchGameResponse responseDataLaunchGame = api.launchGame(username, 112, "html5-desktop", "1", "SEX001");

            logger.info("TestRegisterUserCasino = " + responsedata.getStatus());
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("status", responseDataLaunchGame.getStatus());
                jsonObject.put("message", responseDataLaunchGame.getErrorMessage());
                jsonObject.put("gameUrl", responseDataLaunchGame.getGame_url());
                jsonObject.put("balance", responseDataBalance.getBalance());
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
            response.getWriter().println(jsonObject.toString());
        }
    }

    public static class GetTopTaiXiuServerlet
            extends HttpServlet {
        private static final long serialVersionUID = 1L;

        protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
            this.onExecute(request, response);
        }

        protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
            this.onExecute(request, response);
        }

        private void onExecute(HttpServletRequest request, HttpServletResponse response) throws IOException {
            TaiXiuServiceImpl service = new TaiXiuServiceImpl();
            JSONObject jsonObjectAll = new JSONObject();
            try {
                List<TopWin> result = service.getTopWin(1);

                JSONArray jsonArray = new JSONArray();

                for (TopWin topWin : result) {
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("fullname", topWin.getUsername());
                    jsonObject.put("money", topWin.getMoney());
                    jsonArray.put(jsonObject);
                }

                jsonObjectAll.put("code", 200);
                jsonObjectAll.put("data", jsonArray);
            } catch (Exception e) {
                e.printStackTrace();
            }
            response.getWriter().println(jsonObjectAll.toString());
        }
    }

}
