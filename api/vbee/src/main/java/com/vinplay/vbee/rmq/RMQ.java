package com.vinplay.vbee.rmq;

import java.io.BufferedReader;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Properties;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.vinplay.vbee.common.config.VBeePath;
import com.vinplay.vbee.common.rmq.RMQConnectionFactory;
import com.vinplay.vbee.common.statics.Consts;

public class RMQ {
	private static final Logger logger = Logger.getLogger("vbee");

	public static void start() {
		logger.info("STARTING RABBITMQ ...!!!");
		try {
			File file = new File(VBeePath.basePath + "config/rabbitmq_config.xml");
			DocumentBuilderFactory dFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(file);
			doc.getDocumentElement().normalize();
//            System.out.println(doc.getDocumentElement().getNodeName());
			NodeList nodeList = doc.getElementsByTagName("rabbitmq");
			try (BufferedReader br = Files.newBufferedReader(Paths.get(Consts.RMQ_CONFIG_FILE))) {
				Properties prop = new Properties();
				prop.load(br);
				RMQConnectionFactory.USERNAME = prop.getProperty("rmq_username");
				RMQConnectionFactory.PASSWORD = prop.getProperty("rmq_password");
				RMQConnectionFactory.SERVER_ADR = prop.getProperty("rmq_server");
				RMQConnectionFactory.SERVER_PORT = Integer.parseInt(prop.getProperty("rmq_port"));
				RMQConnectionFactory.RECONNECT_DELAY = Integer.parseInt(prop.getProperty("rmq_reconnect_delay"));
				RMQConnectionFactory.CONNECTION_TIMEOUT = Integer.parseInt(prop.getProperty("rmq_connection_timeout"));
				RMQConnectionFactory.HANDSHAKE_TIMEOUT = Integer.parseInt(prop.getProperty("rmq_handshake_timeout"));
			}catch (Exception e) {
				logger.error("Start RabbitMQ error: ", e);
			}
			Element el = (Element) nodeList.item(0);
			Element queues = (Element) el.getElementsByTagName("queues").item(0);
			NodeList queueList = queues.getElementsByTagName("queue");
			for (int i = 0; i < queueList.getLength(); ++i) {
				RMQ.startConsumer((Element) queueList.item(i));
			}
		} catch (Exception e) {
			logger.error("Start RabbitMQ error: ", (Throwable) e);
		}
		logger.info("RABBITMQ Started ...!!!");
	}

	private static void startConsumer(Element config) {
		String queueName = config.getElementsByTagName("name").item(0).getTextContent();
		int numThreads = Integer.parseInt(config.getElementsByTagName("threads").item(0).getTextContent());
		Element cmds = (Element) config.getElementsByTagName("commands").item(0);
		NodeList cmdList = cmds.getElementsByTagName("command");
		HashMap<Integer, String> commandsMap = new HashMap<Integer, String>();
		for (int i = 0; i < cmdList.getLength(); ++i) {
			Element eCmd = (Element) cmdList.item(i);
			Integer id = Integer.parseInt(eCmd.getElementsByTagName("id").item(0).getTextContent());
			String path = eCmd.getElementsByTagName("path").item(0).getTextContent().trim();
			logger.info("queueName:" + queueName + " id:" + id + " path:" + path);
			commandsMap.put(id, path);
		}
		RMQConsumer consumer = new RMQConsumer(queueName, numThreads);
		consumer.start(commandsMap);
		logger.info(("Start " + numThreads + " " + queueName + ", num commands= " + commandsMap.size() + " success"));
	}
}
