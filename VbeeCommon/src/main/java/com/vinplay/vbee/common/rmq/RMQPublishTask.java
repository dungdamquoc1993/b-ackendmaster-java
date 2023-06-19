package com.vinplay.vbee.common.rmq;

import java.io.IOException;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.vinplay.vbee.common.messages.BaseMessage;

public class RMQPublishTask
extends RMQTask {
    private BaseMessage message;
    private int command;

    public RMQPublishTask(BaseMessage message, String queueName, int command) {
        super(queueName);
        this.message = message;
        this.command = command;
    }

    @Override
    public void run(Channel channel) throws IOException {
        channel.basicPublish("", this.queueName, new AMQP.BasicProperties("text/plain", "UTF-8", null, Integer.valueOf(2), Integer.valueOf(0), null, null, null, String.valueOf(this.command), null, null, null, null, null), this.message.toBytes());
    }
}

