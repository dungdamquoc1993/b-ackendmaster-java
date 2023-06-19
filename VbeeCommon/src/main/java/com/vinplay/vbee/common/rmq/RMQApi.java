package com.vinplay.vbee.common.rmq;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Properties;
import java.util.concurrent.TimeoutException;

import com.vinplay.vbee.common.messages.BaseMessage;
import com.vinplay.vbee.common.messages.LogMoneyUserMessage;
import com.vinplay.vbee.common.statics.Consts;

public class RMQApi {
    public static void publishMessage(String queueName, BaseMessage message, int command) throws IOException, TimeoutException, InterruptedException {
        RMQPublishTask task = new RMQPublishTask(message, queueName, command);
        task.start();
    }

    public static void publishMessagePayment(BaseMessage message, int command) throws IOException, TimeoutException, InterruptedException {
        String queueName = "queue_payment";
        switch (command) {
            case 16: {
                queueName = "queue_payment_minigame";
                command = 30;
                break;
            }
            case 10: {
                queueName = "queue_payment_gamebai";
                command = 40;
                break;
            }
            case 12: {
                queueName = "queue_payment_gamebai";
                command = 41;
                break;
            }
            case 13: {
                queueName = "queue_payment_gamebai";
                command = 42;
            }
        }
        RMQPublishTask task = new RMQPublishTask(message, queueName, command);
        task.start();
    }

    public static void publishMessageLogMoney(LogMoneyUserMessage message) throws IOException, TimeoutException, InterruptedException {
        RMQPublishTask task = new RMQPublishTask(message, "queue_log_money", 601);
        task.start();

        RMQPublishTask taskReportUser = new RMQPublishTask(message, "queue_log_report_user_balance", 602);
        taskReportUser.start();

        RMQPublishTask taskExtra = new RMQPublishTask(message, "queue_log_money_extra", 1001);
        taskExtra.start();
    }

    public static void start(String configFile) throws IOException {
        if (configFile == null || configFile.isEmpty()) {
            configFile = Consts.RMQ_CONFIG_FILE;
        }
		try (BufferedReader br = Files.newBufferedReader(Paths.get(configFile))) {
			Properties prop = new Properties();
			prop.load(br);
			RMQConnectionFactory.USERNAME = prop.getProperty("rmq_username");
			RMQConnectionFactory.PASSWORD = prop.getProperty("rmq_password");
			RMQConnectionFactory.SERVER_ADR = prop.getProperty("rmq_server");
			RMQConnectionFactory.SERVER_PORT = Integer.parseInt(prop.getProperty("rmq_port"));
			RMQConnectionFactory.RECONNECT_DELAY = Integer.parseInt(prop.getProperty("rmq_reconnect_delay"));
			RMQConnectionFactory.CONNECTION_TIMEOUT = Integer.parseInt(prop.getProperty("rmq_connection_timeout"));
			RMQConnectionFactory.HANDSHAKE_TIMEOUT = Integer.parseInt(prop.getProperty("rmq_handshake_timeout"));
		}
    }
}

