package com.vinplay.api.server;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import javax.servlet.DispatcherType;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.server.handler.HandlerCollection;
import org.eclipse.jetty.servlet.ServletHandler;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import com.vinplay.api.processors.vippoint.TopVippoint;
import com.vinplay.api.utils.PortalUtils;
import com.vinplay.dal.service.LogPortalService;
import com.vinplay.dal.service.impl.LogPortalServiceImpl;
import com.vinplay.dal.utils.PotUtils;
import com.vinplay.vbee.common.config.VBeePath;
import com.vinplay.vbee.common.cp.BaseController;
import com.vinplay.vbee.common.cp.NoCommandRegistered;
import com.vinplay.vbee.common.cp.Param;
import com.vinplay.vbee.common.hazelcast.HazelcastLoader;
import com.vinplay.vbee.common.mongodb.MongoDBConnectionFactory;
import com.vinplay.vbee.common.rmq.RMQApi;
import com.vinplay.vbee.common.statics.Consts;
import com.vinplay.vbee.common.utils.UserValidaton;

public class JettyServer {
	private static final Logger logger = Logger.getLogger("api");
	private static final Logger blackListIpLogger = Logger.getLogger("BlackListIpLogger");
	private static final String LOG_PROPERTIES_FILE = "config/log4j.properties";
	private static LogPortalService service = new LogPortalServiceImpl();
	private static String API_PORT = "8081";
	private static BaseController<HttpServletRequest, String> controller;
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
			basePath = VBeePath.initBasePath(JettyServer.class);
			JettyServer.initializeLogger();
			logger.debug("STARTING PORTAL API SERVER .... !!!!");
			JettyServer.loadCommands();
			RMQApi.start(Consts.RMQ_CONFIG_FILE);
			// Config
			HazelcastLoader.start();
			MongoDBConnectionFactory.init();
			UserValidaton.init();
			PortalUtils.loadGameConfig();
			PotUtils.init();
			int port = Integer.parseInt(API_PORT);
			Server server = new Server();
			ServerConnector connector = new ServerConnector(server);
			connector.setPort(port);
			connector.setIdleTimeout(30000L);
			ServletHandler handler = new ServletHandler();
			handler.addFilterWithMapping(CorsFilter.class, "/*", EnumSet.of(DispatcherType.REQUEST));
			handler.addServletWithMapping(JeetyServlet.class, "/api");
			handler.addServletWithMapping(DownloadServlet.class, "/download");
			HandlerCollection handlerCollection = new HandlerCollection();
			handlerCollection.setHandlers(new Handler[] { handler });
			server.setHandler(handlerCollection);
			server.addConnector(connector);
			server.start();
			logger.info("PORTAL API SERVER Started ...!!!");
			server.join();
		} catch (Exception e) {
			logger.info(("PORTAL API SERVER Start error: " + e.getMessage()));
			e.printStackTrace();
		}
	}

	private static void loadCommands() throws Exception {
		File file = new File(basePath.concat("config/api_portal.xml"));
		DocumentBuilderFactory dFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder = dFactory.newDocumentBuilder();
		Document doc = dBuilder.parse(file);
		doc.getDocumentElement().normalize();
		NodeList nodeList = doc.getElementsByTagName("portal");
		Element el = (Element) nodeList.item(0);
		API_PORT = el.getElementsByTagName("port").item(0).getTextContent();
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
		controller = new BaseController<HttpServletRequest, String>();
		controller.initCommands(commandsMap);
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

		private void onExecute(HttpServletRequest request, HttpServletResponse response) throws IOException {
			response.setContentType("text/html");
			response.setCharacterEncoding("UTF-8");
			response.setStatus(200);
			Map<String, String[]> requestMap = request.getParameterMap();
			String remoteAddr = request.getRemoteAddr();
			logger.info("IP:" + remoteAddr);
			if (requestMap.containsKey("c")) {
				String command = request.getParameter("c");
				if (command == null || command.equalsIgnoreCase("")) {
					blackListIpLogger.debug(remoteAddr);
					return;
				}
				Param<HttpServletRequest> param = new Param<>();
				param.set(request);
				logger.debug("command: " + command);
				try {
					if (!"8000".equals(command)) {
						response.getWriter()
								.println(controller.processCommand(Integer.valueOf(Integer.parseInt(command)), param));
						service.log(command);
					} else {
						response.setContentType("text/csv");
						response.setHeader("Content-Disposition", "attachment; filename=\"export.csv\"");
						OutputStream outputStream = response.getOutputStream();
						String outputResult = controller.processCommand(Integer.valueOf(Integer.parseInt(command)),
								param);
						outputStream.write(outputResult.getBytes());
						outputStream.flush();
						outputStream.close();
					}
				} catch (NoCommandRegistered e2) {
					logger.debug("COMMAND NOT FOUND");
					response.getWriter().println("COMMAND NOT FOUND");
					service.log("CMD_404");
				} catch (Exception e1) {
					e1.printStackTrace();
					System.out.println(e1);
					response.getWriter().println("EXCEPTION: " + e1.getMessage());
				}
			} else {
				blackListIpLogger.debug(remoteAddr);
				response.getWriter().println("NO COMMANDS PARAMETERS");
				service.log("NO_CMD");
			}
		}
	}

	public static class DownloadServlet extends HttpServlet {
		private static final long serialVersionUID = 1L;

		public void doGet(HttpServletRequest request, HttpServletResponse response)
				throws ServletException, IOException {
			String localDirConfig = "";
			try (BufferedReader br = Files.newBufferedReader(Paths.get(Consts.FOLDER_UPLOAD_APP))) {
				Properties prop = new Properties();
				prop.load(br);
				localDirConfig = prop.getProperty("download");
			}catch (Exception e) {
				localDirConfig = "/var/app/config/cdn";
			}

			File folder = new File(localDirConfig);
			File file = null;
			for (File f : folder.listFiles()) {
				if(f.isFile()) {
					file =f;
					break;
				}
			}

			response.setContentType("text/html");
			PrintWriter out = response.getWriter();
			response.setContentType("APPLICATION/OCTET-STREAM");
			response.setHeader("Content-Disposition", "attachment; filename=\"" + file.getName() + "\"");
			FileInputStream fileInputStream = new FileInputStream(file.getAbsoluteFile());
			int i;
			while ((i = fileInputStream.read()) != -1) {
				out.write(i);
			}
			fileInputStream.close();
			out.close();
		}
	}
}
