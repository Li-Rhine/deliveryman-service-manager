package com.imooc.deliverymanservicemanager.service;

import com.fasterxml.jackson.databind.ObjectMapper;

import com.imooc.deliverymanservicemanager.dao.DeliverymanDao;
import com.imooc.deliverymanservicemanager.dto.OrderMessageDTO;
import com.imooc.deliverymanservicemanager.enummeration.DeliverymanStatus;
import com.imooc.deliverymanservicemanager.po.DeliverymanPO;
import com.rabbitmq.client.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeoutException;

/**
 * @Description：
 * @Author： Rhine
 * @Date： 2020/11/22 16:49
 **/
@Slf4j
@Service
public class OrderMessageService {

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    DeliverymanDao deliverymanDao;

    @Async
    public void handleMessage() throws IOException, TimeoutException, InterruptedException {
        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setHost("101.132.104.74");
        try(Connection connection = connectionFactory.newConnection();
            Channel channel = connection.createChannel();) {

            channel.exchangeDeclare(
                    "exchange.order.deliveryman",
                    BuiltinExchangeType.DIRECT,
                    true,
                    false,
                    null
            );
            channel.queueDeclare(
                    "queue.deliveryman",
                    true,
                    false,
                    false,
                    null
            );
            channel.queueBind(
                    "queue.deliveryman",
                    "exchange.order.deliveryman",
                    "key.deliveryman"
            );

            channel.basicConsume("queue.deliveryman", true, deliverCallback, consumerTag -> {});
            while (true) {
                Thread.sleep(100000);
            }
        }
    }


    //具体消费逻辑
    DeliverCallback deliverCallback = ((consumerTag, message) -> {
        String messageBody = new String(message.getBody());

        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setHost("101.132.104.74");

        try{
            //将消息反序列化
            OrderMessageDTO orderMessageDTO = objectMapper.readValue(messageBody, OrderMessageDTO.class);

            List<DeliverymanPO> deliverymanPOS = deliverymanDao.selectDeliverymanByStatus(DeliverymanStatus.AVALIABLE);
            orderMessageDTO.setDeliverymanId(deliverymanPOS.get(0).getId());

            log.info("onMessage:{}", orderMessageDTO);

            try(Connection connection = connectionFactory.newConnection();
                Channel channel = connection.createChannel();) {

                String messageToSend = objectMapper.writeValueAsString(orderMessageDTO);
                channel.basicPublish(
                        "exchange.order.deliveryman",
                        "key.order",
                        null,
                        messageToSend.getBytes());
            }


        }catch (Exception e) {
            log.error(e.getMessage(), e);
        }


    });
            
            
            
}
