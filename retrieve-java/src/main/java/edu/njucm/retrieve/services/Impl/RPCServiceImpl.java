package edu.njucm.retrieve.services.Impl;


import edu.njucm.retrieve.conf.RabbitConf;
import edu.njucm.retrieve.services.RPCService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageBuilder;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;

@Service
public class RPCServiceImpl implements RPCService {


    private static final Logger logger = LoggerFactory.getLogger(RPCServiceImpl.class);

    @Autowired
    private RabbitTemplate rabbitTemplate;


    @Override
    public String sendAndReceive(String message) {
        Message newMessage = MessageBuilder.withBody(message.getBytes()).build();
        rabbitTemplate.setReplyTimeout(600000);
        //发送消息
        Message result = rabbitTemplate.sendAndReceive(RabbitConf.RPC_EXCHANGE, RabbitConf.RPC_QUEUE1, newMessage);
        //获取校验码
        String correlationId = newMessage.getMessageProperties().getCorrelationId();
        logger.info("Send PATH \"{}\" to Python,correlationId={}", new String(newMessage.getBody()), correlationId);
        if (result != null) {
            // 获取响应头信息
            HashMap<String, Object> headers = (HashMap<String, Object>) result.getMessageProperties().getHeaders();
            //获取结果校验码
            String msgId = String.valueOf(result.getMessageProperties().getDeliveryTag());
            logger.info("Already received result of \"{}\",correlationId={}", new String(newMessage.getBody()), msgId);
            logger.info("\"{}\" passed", new String(newMessage.getBody()));
            return new String(result.getBody());

        } else
            return null;
    }
}
