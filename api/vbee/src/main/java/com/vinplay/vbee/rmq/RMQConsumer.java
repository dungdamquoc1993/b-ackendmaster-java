package com.vinplay.vbee.rmq;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.Consumer;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;
import com.vinplay.vbee.common.cp.BaseController;
import com.vinplay.vbee.common.cp.NoCommandRegistered;
import com.vinplay.vbee.common.cp.Param;
import com.vinplay.vbee.common.rmq.RMQConnectionFactory;
import com.vinplay.vbee.logger.HandleMessageLogger;
import java.io.IOException;
import java.util.Date;
import java.util.Map;
import org.apache.log4j.Logger;

public class RMQConsumer {
    private static final int PREFETCH_COUNT = 20;
    private static final Logger logger = Logger.getLogger((String)"vbee");
    private BaseController<byte[], Boolean> controller;
    private String queueName;
    private int numConsumer = 1;

    public RMQConsumer(String queueName, int numConsumer) {
        this.queueName = queueName;
        if (numConsumer > 1) {
            this.numConsumer = numConsumer;
        }
    }

    public void start(Map<Integer, String> commandMap) {
        try {
            this.controller = new BaseController<>();
            commandMap.forEach((k,v)->{
            	logger.debug("  " + k + " - " + v);
            });
			try {
				this.controller.initCommands(commandMap);
			} catch (Exception e) {
				e.printStackTrace();
				logger.error((Object) e.getMessage());
			}
			// create connection
			Connection connection = RMQConnectionFactory.newConnection();
			// create channel
			Channel channel = connection.createChannel();
			// create queue name
			channel.queueDeclare(this.queueName, true, false, false, null);
			this.run(channel);
        }
        catch (Exception e) {
            e.printStackTrace();
            logger.error((Object)e.getMessage());
        }
    }

    private void run(final Channel channel) throws IOException {
        channel.basicQos(PREFETCH_COUNT);// Per consumer limit
        DefaultConsumer consumer = new DefaultConsumer(channel){

            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                long startHandleTime = System.currentTimeMillis();
                int command = Integer.valueOf(properties.getMessageId());
				try {
					logger.debug(String.valueOf(RMQConsumer.this.queueName) + " HANDLE MESSAGE: " + command);
					Param<byte[]> p = new Param<byte[]>();
					p.set(body);
					RMQConsumer.this.controller.processCommand(command, p);
					channel.basicAck(envelope.getDeliveryTag(), false);
					
				} catch (NoCommandRegistered e2) {
					logger.error("COMMAND ZZZ: " + command + " NOT FOUND IN QUEUE: "
							+ RMQConsumer.this.queueName);
				}
                catch (Exception e) {
                    e.printStackTrace();
                    logger.error((String.valueOf(RMQConsumer.this.queueName) + " HANDLE MESSAGE: " + command + " ERROR: "), (Throwable)e);
                }
                long endHandleTime = System.currentTimeMillis();
                long handleTime = endHandleTime - startHandleTime;
                HandleMessageLogger.log(RMQConsumer.this.queueName, command, handleTime, new Date());
            }
        };
        for (int i = 0; i < this.numConsumer; ++i) {
            channel.basicConsume(this.queueName, false, (Consumer)consumer);
        }
    }

}

