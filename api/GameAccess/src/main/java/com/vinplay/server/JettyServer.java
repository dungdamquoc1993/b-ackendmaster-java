package com.vinplay.server;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.EnumSet;
import java.util.Properties;

import javax.servlet.DispatcherType;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.server.handler.HandlerCollection;
import org.eclipse.jetty.servlet.ServletHandler;
import org.json.JSONException;

import com.vinplay.api.AgGamesApi;
import com.vinplay.api.EbetGamesApi;
import com.vinplay.api.EbetVerifyApi;
//import com.vinplay.api.CmdGamesApi;
//import com.vinplay.api.CmdVerifyApi;
import com.vinplay.api.GameBalanceApi;
import com.vinplay.api.Ibc2GamesApi;
import com.vinplay.api.SBOGamesApi;
//import com.vinplay.api.TransferGameBalanceApi;
import com.vinplay.api.WmGamesApi;
import com.vinplay.run.ScheduleMain;
import com.vinplay.usercore.utils.GameCommon;
import com.vinplay.usercore.utils.GameThirdPartyInit;
import com.vinplay.vbee.common.config.VBeePath;
import com.vinplay.vbee.common.hazelcast.HazelcastLoader;
import com.vinplay.vbee.common.mongodb.MongoDBConnectionFactory;
import com.vinplay.vbee.common.rmq.RMQApi;
import com.vinplay.vbee.common.statics.Consts;

public class JettyServer {
	private static final Logger logger = Logger.getLogger("api");
	private static final Logger blackListIpLogger = Logger.getLogger("BlackListIpLogger");
	private static final String LOG_PROPERTIES_FILE = "config/log4j.properties";
	private static final String API_PORT = "9112";
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
	public static void loadGameConfig() throws SQLException, JSONException, ParseException {
		GameCommon.init();
		 //add game3rd config
		GameThirdPartyInit.init();
		
		ScheduleMain.run();
	}

	public static void main(String[] args) {
		try {
			basePath = VBeePath.initBasePath(JettyServer.class);
			JettyServer.initializeLogger();
			logger.debug("STARTING GAMEACCESS API SERVER .... !!!!");
			RMQApi.start(Consts.RMQ_CONFIG_FILE);
			// Config
			HazelcastLoader.start();
			MongoDBConnectionFactory.init();
			loadGameConfig();
			int port = Integer.parseInt(API_PORT);
			Server server = new Server();
			ServerConnector connector = new ServerConnector(server);
			connector.setPort(port);
			connector.setIdleTimeout(30000L);
			ServletHandler handler = new ServletHandler();
			handler.addFilterWithMapping(CorsFilter.class, "/*", EnumSet.of(DispatcherType.REQUEST));
			handler.addServletWithMapping(Ibc2GamesApi.class, "/3rd/ibc");
			handler.addServletWithMapping(AgGamesApi.class, "/3rd/ag");
			handler.addServletWithMapping(WmGamesApi.class, "/3rd/wm");
//			handler.addServletWithMapping(CmdGamesApi.class, "/3rd/cmd");
//			handler.addServletWithMapping(CmdVerifyApi.class, "/3rd/verify/cmd");
			handler.addServletWithMapping(GameBalanceApi.class, "/3rd/balance");
			handler.addServletWithMapping(EbetGamesApi.class, "/3rd/ebet");
			handler.addServletWithMapping(SBOGamesApi.class, "/3rd/sbo");
			handler.addServletWithMapping(EbetVerifyApi.class, "/3rd/eb/verify");
			
//			handler.addServletWithMapping(TransferGameBalanceApi.class, "/3rd/transfer");
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

}
