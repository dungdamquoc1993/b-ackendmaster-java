package com.vinplay.api.backend.server;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.*;

import javax.servlet.DispatcherType;
import javax.servlet.MultipartConfigElement;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IMap;
import com.vinplay.usercore.service.impl.OtpServiceImpl;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.HandlerCollection;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.vinplay.api.backend.processors.userMission.ResetUserMissionUtils;
import com.vinplay.api.backend.report.utils.BackendUtils;
import com.vinplay.api.backend.report.utils.ReportMoneyUtils;
import com.vinplay.shotfish.entites.TeleBotConfig;
import com.vinplay.usercore.dao.UserDao;
import com.vinplay.usercore.dao.impl.UserDaoImpl;
import com.vinplay.usercore.utils.CacheConfigName;
import com.vinplay.usercore.utils.GameCommon;
import com.vinplay.usercore.utils.GameThirdPartyInit;
import com.vinplay.vbee.common.config.VBeePath;
import com.vinplay.vbee.common.cp.BaseController;
import com.vinplay.vbee.common.cp.NoCommandRegistered;
import com.vinplay.vbee.common.cp.Param;
import com.vinplay.vbee.common.hazelcast.HazelcastClientFactory;
import com.vinplay.vbee.common.hazelcast.HazelcastLoader;
import com.vinplay.vbee.common.models.UserModel;
import com.vinplay.vbee.common.models.cache.UserCacheModel;
import com.vinplay.vbee.common.mongodb.MongoDBConnectionFactory;
import com.vinplay.vbee.common.response.BaseResponse;
import com.vinplay.vbee.common.response.BaseResponseModel;
import com.vinplay.vbee.common.rmq.RMQApi;
import com.vinplay.vbee.common.statics.Consts;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class VinPlayBackendMain {
    private static final Logger logger = Logger.getLogger("backend");
    private static final String LOG_PROPERTIES_FILE = "config/log4j.properties";
    private static int API_PORT = 19082;
    private static BaseController<HttpServletRequest, String> controller;
    public static int TYPE;
    private static String basePath;

    private static void initializeLogger() {
        Properties logProperties = new Properties();
        try {
            File file = new File(basePath.concat(LOG_PROPERTIES_FILE));
            logProperties.load(new FileInputStream(file));
            PropertyConfigurator.configure((Properties) logProperties);
            logger.info("Logging initialized.");
        } catch (IOException e) {
            throw new RuntimeException("Unable to load logging property config/log4j.properties");
        }
    }

    public static void main(String[] args) {
        try {
            basePath = VBeePath.initBasePath(VinPlayBackendMain.class);
            VinPlayBackendMain.initializeLogger();
            logger.debug("STARTING BACKEND API SERVER .... !!!!");

            // Config
            VinPlayBackendMain.loadCommands();
            HazelcastLoader.start();
            MongoDBConnectionFactory.init();
            RMQApi.start(Consts.RMQ_CONFIG_FILE);
            GameCommon.init();

            // add game3rd config
            GameThirdPartyInit.init();
            if (BackendUtils.apiRunTask()) {
                ReportMoneyUtils.init();
                ResetUserMissionUtils.init();
            }

            ScheduleMain.run();
            Server server = new Server(API_PORT);
            ServletHandler handler = new ServletHandler();
            handler.addFilterWithMapping(CorsFilter.class, "/*", EnumSet.of(DispatcherType.REQUEST));
            handler.addServletWithMapping(JeetyServlet.class, "/api_backend");
            handler.addServletWithMapping(VerifyMobileViaTelegramServlet.class, "/verify/telegram");
            handler.addServletWithMapping(APIAgentServlet.class, "/api_agent");

////          Register folder upload
//			ServletContextHandler context = new ServletContextHandler();
//			try {
//				context.addFilter(CorsFilter.class, "/*", EnumSet.of(DispatcherType.REQUEST));
//				ServletHolder uploadHolder = context.addServlet(UploadServlet.class, "/upload");
//				String localDirConfigUpload = "";
//				String localDirConfigDownload = "";
//				long maxFileSize = 1024 * 1024 * 50;
//				long maxRequestSize = -1L;
//				int fileSizeThreshold = 1024 * 1024;
//				try (BufferedReader br = Files.newBufferedReader(Paths.get(Consts.FOLDER_UPLOAD_APP))) {
//					Properties prop = new Properties();
//					prop.load(br);
//					localDirConfigUpload = prop.getProperty("upload");
//					localDirConfigDownload = prop.getProperty("download");
//					maxFileSize = 1024 * 1024 * Long.parseLong(prop.getProperty("maxFileSize"));
//					maxRequestSize = Long.parseLong(prop.getProperty("maxRequestSize")) == -1 ? -1
//							: 1024 * 1024 * Long.parseLong(prop.getProperty("maxRequestSize"));
//					fileSizeThreshold = 1024 * 1024 * Integer.parseInt(prop.getProperty("fileSizeThreshold"));
//				}catch (Exception e) {
//					localDirConfigUpload = "/var/app/config/cdn/upload";
//					localDirConfigDownload = "/var/app/config/cdn";
//					maxFileSize = 1024 * 1024 * 50;
//					maxRequestSize = -1L;
//					fileSizeThreshold = 1024 * 1024;
//				}
//				
//				File locationDirDownload = new File(localDirConfigDownload);
//				if (!locationDirDownload.exists())
//					locationDirDownload.mkdirs();
//				
//				File locationDirUpload = new File(localDirConfigUpload);
//				if (!locationDirUpload.exists())
//					locationDirUpload.mkdirs();
//	
//				String location = locationDirUpload.getAbsolutePath();
//				MultipartConfigElement multipartConfig = new MultipartConfigElement(location, maxFileSize, maxRequestSize,
//						fileSizeThreshold);
//				uploadHolder.getRegistration().setMultipartConfig(multipartConfig);
//			}catch (Exception e) {
//				e.printStackTrace();
//				logger.info(("Register up/down load file error: " + e.getMessage()));
//			}
//			
//			HandlerCollection collection = new HandlerCollection();
//			collection.addHandler(context);
//			collection.addHandler(handler);
//			server.setHandler(collection);
            server.setHandler(handler);
            server.start();
            logger.info("BACKEND API SERVER Started ...!!!");
            server.join();
        } catch (Exception e) {
            e.printStackTrace();
            logger.info(("BACKEND API SERVER Start error: " + e.getMessage()));
        }
    }

    private static void loadCommands() throws Exception {
        File file = new File(basePath.concat("config/api_backend.xml"));
        DocumentBuilderFactory dFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dFactory.newDocumentBuilder();
        Document doc = dBuilder.parse(file);
        doc.getDocumentElement().normalize();
        NodeList nodeList = doc.getElementsByTagName("backend");
        Element el = (Element) nodeList.item(0);
        API_PORT = Integer.parseInt(el.getElementsByTagName("port").item(0).getTextContent());
        try {
            TYPE = Integer.parseInt(el.getElementsByTagName("type").item(0).getTextContent());
        } catch (Exception e) {
            e.printStackTrace();
        }
        Element cmds = (Element) el.getElementsByTagName("commands").item(0);
        NodeList cmdList = cmds.getElementsByTagName("command");
        HashMap<Integer, String> commandsMap = new HashMap<Integer, String>();
        for (int i = 0; i < cmdList.getLength(); ++i) {
            Element eCmd = (Element) cmdList.item(i);
            Integer id = Integer.parseInt(eCmd.getElementsByTagName("id").item(0).getTextContent());
            String path = eCmd.getElementsByTagName("path").item(0).getTextContent();
            System.out.println(id + " <-> " + path);
            commandsMap.put(id, path);
        }
        if (controller == null) {
            controller = new BaseController<>();
            controller.initCommands(commandsMap);
        }
    }

    static {
        TYPE = 0;
    }

    public static class JeetyServlet extends HttpServlet {
        private static final long serialVersionUID = 1L;

        protected void doGet(HttpServletRequest request, HttpServletResponse response)
                throws ServletException, IOException {
            this.onExecute(request, response);
        }

        protected void doPost(HttpServletRequest request, HttpServletResponse response)
                throws ServletException, IOException {
            this.onExecute(request, response);
        }

        private String getIpAddress(HttpServletRequest request) {
            String ip = request.getHeader("x-forwarded-for");
            if ((ip == null) || (ip.length() == 0) || ("unknown".equalsIgnoreCase(ip))) {
                ip = request.getHeader("Proxy-Client-IP");
            }
            if ((ip == null) || (ip.length() == 0) || ("unknown".equalsIgnoreCase(ip))) {
                ip = request.getHeader("WL-Proxy-Client-IP");
            }
            if ((ip == null) || (ip.length() == 0) || ("unknown".equalsIgnoreCase(ip))) {
                ip = request.getRemoteAddr();
            }
            return ip;
        }

        private void onExecute(HttpServletRequest request, HttpServletResponse response) throws IOException {
            response.setContentType("text/html");
            response.setCharacterEncoding("UTF-8");
            response.setStatus(200);
            Map<String, String[]> requestMap = request.getParameterMap();
            String remoteAddr = getIpAddress(request);

            logger.info(String.format("remoteAddr: %s - %s - %s ", remoteAddr, request.getRemoteAddr(),
                    request.getQueryString()));

            if ("dev".equals(GameThirdPartyInit.enviroment)) {
                if (!Consts.IP_OFFCIE.contains(remoteAddr)) {
                    response.getWriter().println("Your ip address is not allow " + remoteAddr);
                    return;
                }

            } else {
                if (!Consts.IP_SERVER.contains(remoteAddr)) {
                    response.getWriter().println("Your ip address is not allow " + remoteAddr);
                    return;
                }
            }
            if (requestMap.containsKey("c")) {
                String command = request.getParameter("c");
                Param<HttpServletRequest> param = new Param<>();
                param.set(request);
                // logger.debug(("command: " + command));
                try {
                    String responseString = (String) controller.processCommand(Integer.valueOf(command), param);
                    response.getWriter().println(responseString);
                } catch (NoCommandRegistered e2) {
                    logger.debug("COMMAND NOT FOUND");
                    response.getWriter().println("COMMAND NOT FOUND");
                } catch (Exception e1) {
                    e1.printStackTrace();
                    logger.error(e1);
                    if (controller == null) {
                        logger.info("controller null");
                        response.getWriter().println(
                                "EXCEPTION: controller - " + e1.getMessage() + " | " + request.getQueryString());
                    } else {
                        response.getWriter()
                                .println("EXCEPTION: " + e1.getMessage() + " | " + request.getQueryString());
                    }
                }
            } else {
                response.getWriter().println("NO COMMANDS PARAMETERS");
            }
        }
    }

    public static class APIAgentServlet extends HttpServlet {
        private static final long serialVersionUID = 1L;

        protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
            this.onExecute(request, response);
        }

        protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
            this.onExecute(request, response);
        }

        private String getIpAddress(HttpServletRequest request) {
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

        private void onExecute(HttpServletRequest request, HttpServletResponse response) throws IOException {
            response.setContentType("text/html");
            response.setCharacterEncoding("UTF-8");
            response.setStatus(200);
            Map<String, String[]> requestMap = request.getParameterMap();
            String remoteAddr = getIpAddress(request);
            logger.info(String.format("remoteAddr: %s - %s - %s ", remoteAddr, request.getRemoteAddr(), request.getQueryString()));


//            if ("dev".equals(GameThirdPartyInit.enviroment)) {
//                if (!Consts.IP_OFFCIE.contains(remoteAddr)) {
//                    response.getWriter().println("Your ip address is not allow " + remoteAddr);
//                    return;
//                }
//            } else {
//                if (!Consts.IP_DAILY.contains(remoteAddr)) {
//                    response.getWriter().println("Your ip address is not allow " + remoteAddr);
//                    return;
//                }
//            }

            if (requestMap.containsKey("c")) {
                String command = request.getParameter("c");
                List<String> commands = Arrays.asList("9424", "9425", "9426", "9427", "9428", "9431",
                        "8840", "72", "73", "74", "75", "9440", "9441", "9442", "9443", "9444", "9445", "9446",
                        "9447", "9448", "9449", "9450");
                if (command == null || command.trim().isEmpty()) {
                    logger.debug("COMMAND NOT FOUND");
                    response.getWriter().println("COMMAND NOT FOUND");
                    return;
                }

//                if (!commands.contains(command)) {
//                    response.getWriter().println("Not allow access this api");
//                    return;
//                }

                Param<HttpServletRequest> param = new Param<>();
                param.set(request);
                try {
                    String responseString = (String) controller.processCommand(Integer.valueOf(Integer.parseInt(command)), param);
                    response.getWriter().println(responseString);
                } catch (NoCommandRegistered e2) {
                    logger.debug("COMMAND NOT FOUND");
                    response.getWriter().println("COMMAND NOT FOUND");
                    return;
                } catch (Exception e1) {
                    e1.printStackTrace();
                    logger.error(e1);
                    if (controller == null) {
                        logger.info("controller null");
                        response.getWriter().println("EXCEPTION: controller - " + e1.getMessage() + " | " + request.getQueryString());
                        return;
                    } else {
                        response.getWriter().println("EXCEPTION: " + e1.getMessage() + " | " + request.getQueryString());
                        return;
                    }
                }
            } else {
                response.getWriter().println("NO COMMANDS PARAMETERS");
                return;
            }
        }
    }

    public static class VerifyMobileViaTelegramServlet extends HttpServlet {
        private static final Logger logger = Logger.getLogger("api");
        private static final long serialVersionUID = 1L;

        protected void doGet(HttpServletRequest request, HttpServletResponse response)
                throws ServletException, IOException {
            logger.info("GET Verify via telegram");
            doPost(request, response);
        }

        protected void doPost(HttpServletRequest request, HttpServletResponse response)
                throws ServletException, IOException {
            logger.info("POST Verify via telegram");
            this.onExecute(request, response);
        }

        private String onExecute(HttpServletRequest request, HttpServletResponse response) throws IOException {
			TeleBotConfig botConfig = getConfig();
			String URLAPITELEGRAM = "https://api.telegram.org/bot" + botConfig.secretKey + "/sendMessage";
			UserDao userDao = new UserDaoImpl();
			int code = 0;
			response.setContentType("application/json");
			response.setCharacterEncoding("UTF-8");
			response.setStatus(200);
			StringBuffer jb = new StringBuffer();
			String line = null;
			try {
				BufferedReader reader = request.getReader();
				while ((line = reader.readLine()) != null)
					jb.append(line);
			} catch (Exception e) {
				logger.error("Verify via telegram: " + e.getMessage());
			}
			
			JsonObject jsonObject = new JsonObject();
			try {
				jsonObject = JsonParser.parseString(jb.toString()).getAsJsonObject();
			} catch (Exception e) {
				e.printStackTrace();
				logger.error("Verify via telegram: " + e.getMessage());
				return new BaseResponseModel<String>(false, "1001", "Error system. Please contact admin to support").toJson();
			}

			String telegramId = "";
			try {
				telegramId = jsonObject.getAsJsonObject("message").getAsJsonObject("chat").get("id").getAsString();
			} catch (Exception e) {
				logger.error("Verify via telegram | Can not get chatId: " + e.getMessage());
				return new BaseResponseModel<String>(false, "1001", "Not found chat session on device of customer").toJson();
			}
			
			//Check command start bot
			String contentChat = "";
			try{
				contentChat = jsonObject.getAsJsonObject("message").get("text").getAsString();
			}catch (Exception e) { }
			
			if(contentChat.contains("/start")) {
				try{
					String[] arr = contentChat.split(" ");
					String accessToken = arr[1].split("-")[0];
					String nickname = arr[1].split("-")[1];
					IMap<String,UserModel> userMap = HazelcastClientFactory.getInstance().getMap("users");
					if (!userMap.containsKey(nickname)) {
						code = postRequest(URLAPITELEGRAM, telegramId, "Thông tin xác thực không đúng. Vui lòng đăng xuất và đăng nhập lại để tiến hành xác thực.");
						return code == 200 ? new BaseResponseModel<String>(true, "0", "success").toJson() 
								: new BaseResponseModel<String>(false, "1001", "Thông tin xác thực không đúng").toJson();
					}
					
					//check token
					UserCacheModel userCache = (UserCacheModel) userMap.get(nickname);
					if (!userCache.getAccessToken().equals(accessToken)) {
						code = postRequest(URLAPITELEGRAM, telegramId, "Thông tin xác thực không đúng. Vui lòng đăng xuất và đăng nhập lại để tiến hành xác thực.");
						return code == 200 ? new BaseResponseModel<String>(true, "0", "success").toJson() 
								: new BaseResponseModel<String>(false, "1001", "Thông tin xác thực không đúng").toJson();
					}
					
					userDao.updateTeleId(telegramId, nickname);
				}catch (Exception e) { 
					logger.error("Verify via telegram | Can not get at: " + e.getMessage());
					return new BaseResponseModel<String>(false, "1001", "Can not get at of client").toJson();
				}
			}

			String phoneNumber = "";
			try {
				phoneNumber = jsonObject.getAsJsonObject("message").getAsJsonObject("contact")
										.get("phone_number").getAsString();
			} catch (Exception e) {
			}

			if (phoneNumber == null || phoneNumber.trim().equals("")) {
				String body = "{\"chat_id\":" + telegramId
						+ ",\"text\":\"Bạn ấn vào nút gửi số điện thoại để xác thực thông tin SUVIP\",\"reply_markup\":{\"keyboard\":[[{\"text\":\"Gửi số điện thoại\",\"request_contact\":true}]],\"one_time_keyboard\":true,\"resize_keyboard\":true}}";
				code = postRequest(URLAPITELEGRAM, body);
				return code == 200 ? new BaseResponseModel<String>(true, "0", "success").toJson() 
						: new BaseResponseModel<String>(false, "1001", "resend").toJson();
			} else {
				phoneNumber = phoneNumber.replace(" ", "");
                phoneNumber = phoneNumber.replace("+", "");
                //Replace 84
				//phoneNumber = phoneNumber.replaceAll("^[0-9]{2}", "0");
				boolean checked = false;
				try {
					checked = userDao.CheckVerifyMobile(phoneNumber);
				} catch (SQLException e) {
				}

				if (checked) {
					code = postRequest(URLAPITELEGRAM, telegramId, "Số điện thoại của bạn đã được xác thực");
					return code == 200 ? new BaseResponseModel<String>(true, "0", "success").toJson() 
							: new BaseResponseModel<String>(false, "1001", "Số điện thoại của bạn đã được xác thực").toJson();
				}

				try {
					checked = userDao.verifyMobileAndTelegramId(phoneNumber, telegramId, true);
				} catch (SQLException e) {
				}

				if (checked) {
					code = postRequest(URLAPITELEGRAM, telegramId, "Số điện thoại của bạn đã được xác thực thành công");
					String nickName = "";
					try {
						nickName = userDao.getNickNameByField("telegram_id", telegramId);
					} catch (Exception e) {
						nickName = "";
					}
					
					if (nickName != null && nickName.trim().equals("") == false)
						updateCached(nickName, phoneNumber);

					return code == 200 ? new BaseResponseModel<String>(true, "0", "success").toJson() 
							: new BaseResponseModel<String>(false, "1001", "Số điện thoại của bạn đã được xác thực thành công").toJson();
				} else {
					code = postRequest(URLAPITELEGRAM, telegramId,
							"Xác thực số điện thoại của bạn không thành công. Vui lòng liên hệ bộ phận CSKH để được hỗ trợ");
					return new BaseResponseModel<String>(true, "0", "success").toJson();
				}
			}
		}


        private void updateCached(String nickName, String mobile) {
            HazelcastInstance client = HazelcastClientFactory.getInstance();
            IMap<String, UserModel> userMap = client.getMap("users");
            if (userMap.containsKey(nickName)) {
                try {
                    userMap.lock(nickName);
                    UserCacheModel user = (UserCacheModel) userMap.get(nickName);
                    user.setMobile(mobile);
                    user.setVerifyMobile(true);
                    userMap.put(nickName, user);
                } finally {
                    userMap.unlock(nickName);
                }
            }
        }

        public static int postRequest(String url, String bodyJson) throws IOException {
            try {
                OkHttpClient client = new OkHttpClient().newBuilder().build();
                MediaType mediaType = MediaType.parse("application/json");
                RequestBody body = RequestBody.create(mediaType, bodyJson);
                Request request = new Request.Builder().url(url).method("POST", body)
                        .addHeader("Content-Type", "application/json").build();
                Response response = client.newCall(request).execute();
                return response.code();
            } catch (Exception e) {
                e.printStackTrace();
                logger.error("postRequest | API: " + url + " | Body: " + bodyJson);
                return 0;
            }
        }

        public static int postRequest(String url, String chatId, String content) throws IOException {
            try {
                OkHttpClient client = new OkHttpClient().newBuilder().build();
                RequestBody body = new MultipartBody.Builder().setType(MultipartBody.FORM)
                        .addFormDataPart("chat_id", chatId).addFormDataPart("text", content).build();
                Request request = new Request.Builder().url(url).method("POST", body).build();
                Response response = client.newCall(request).execute();
                return response.code();
            } catch (Exception e) {
                e.printStackTrace();
                logger.error("postRequest | API: " + url);
                return 0;
            }
        }

        /**
         * Get telegram bot configuration
         *
         * @return ShotfishConfig
         */
        public static TeleBotConfig getConfig() {
            try {
                HazelcastInstance instance = HazelcastClientFactory.getInstance();
                IMap<String, String> configCache = instance.getMap(Consts.CACHE_CONFIG);
                String value = configCache.get(CacheConfigName.TELEGRAMBOTCONFIGCACHE).toString();
                Type type = new TypeToken<TeleBotConfig>() {
                }.getType();
                TeleBotConfig teleBotConfig = new Gson().fromJson(value, type);
                return teleBotConfig;
            } catch (Exception e) {
                logger.error("[GETCONFIG TELEGRAM BOT] Exception: " + e.getMessage());
                return null;
            }
        }
    }

//    public static class UploadServlet extends HttpServlet {
//		private static final long serialVersionUID = 1L;
//
//		public void doGet(HttpServletRequest request, HttpServletResponse response)
//				throws ServletException, IOException {
//			String localDirConfig = "";
//			try (BufferedReader br = Files.newBufferedReader(Paths.get(Consts.FOLDER_UPLOAD_APP))) {
//				Properties prop = new Properties();
//				prop.load(br);
//				localDirConfig = prop.getProperty("download");
//			}catch (Exception e) {
//				localDirConfig = "/var/app/config/cdn";
//			}
//
//			File folder = new File(localDirConfig);
//			File file = null;
//			for (File f : folder.listFiles()) {
//				if(f.isFile()) {
//					file =f;
//					break;
//				}
//			}
//
//			response.setContentType("text/html");
//			PrintWriter out = response.getWriter();
//			response.setContentType("APPLICATION/OCTET-STREAM");
//			response.setHeader("Content-Disposition", "attachment; filename=\"" + file.getName() + "\"");
//			FileInputStream fileInputStream = new FileInputStream(file.getAbsoluteFile());
//			int i;
//			while ((i = fileInputStream.read()) != -1) {
//				out.write(i);
//			}
//			fileInputStream.close();
//			out.close();
//		}
//
//		protected void doPost(HttpServletRequest request, HttpServletResponse response)
//				throws ServletException, IOException {
//			this.onExecute(request, response);
//		}
//
//		private void onExecute(HttpServletRequest request, HttpServletResponse response) throws IOException {
//			PrintWriter out = response.getWriter();
//			response.setContentType("application/json");
//			response.setCharacterEncoding("UTF-8");
//			
//			String result = "";
//			try {
//				if (request.getParts().size() > 1) {
//					result = BaseResponse.error("1002", "Only upload one file per once");
//					out.print(result);
//					out.flush();
//					return;
//				}
//				
//				String localDirUpload = "";
//				String localDirDownload = "";
//				try (BufferedReader br = Files.newBufferedReader(Paths.get(Consts.FOLDER_UPLOAD_APP))) {
//					Properties prop = new Properties();
//					prop.load(br);
//					localDirUpload = prop.getProperty("upload");
//					localDirDownload = prop.getProperty("download");
//				}catch (Exception e) {
//					localDirUpload = "/var/app/config/cdn/upload";
//					localDirDownload = "/var/app/config/cdn";
//				}
//				
//				Arrays.stream(new File(localDirDownload + File.separator).listFiles()).forEach(File::delete);
//				for (Part part : request.getParts()) {
//					String fileName = extractFileName(part);
//					if (fileName != null && fileName.length() > 0) {
//						part.write(File.separator + fileName);
//						File f = new File(localDirUpload + File.separator + fileName);
//						f.renameTo(new File(localDirDownload + File.separator + fileName));
//						result =  BaseResponse.success("0", "success", null);
//						out.print(result);
//						out.flush();
//						return;
//					}
//				}
//
//				result =  BaseResponse.error("1001", "error");
//				out.print(result);
//				out.flush();
//				return;
//			} catch (IOException | ServletException e) {
//				e.printStackTrace();
//				result =  BaseResponse.error("1001", e.getMessage());
//				out.print(result);
//				out.flush();
//				return;
//			}
//		}
//
//		private String extractFileName(Part part) {
//			String contentDisp = part.getHeader("content-disposition");
//			String[] items = contentDisp.split(";");
//			for (String s : items) {
//				if (s.trim().startsWith("filename")) {
//					String clientFileName = s.substring(s.indexOf("=") + 2, s.length() - 1);
//					clientFileName = clientFileName.replace("\\", "/");
//					int i = clientFileName.lastIndexOf('/');
//					return clientFileName.substring(i + 1);
//				}
//			}
//			return null;
//		}
//	}
}
