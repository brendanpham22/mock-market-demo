package com.ascertain.mockdemo.service;


import com.ascertain.mockdemo.entity.Product;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class RabbitMQConsumer {
    private final AmqpTemplate amqpTemplate;

    @Value("${rabbitmq.queue.name}")
    private String queueName;

    @Autowired
    public RabbitMQConsumer(AmqpTemplate amqpTemplate) {
        this.amqpTemplate = amqpTemplate;
    }

    private List<Product> receiveMessage() {
        List<Product> message = (List<Product>) amqpTemplate.receiveAndConvert(queueName);
        return message;
    }

    public List<Product> processMessage() {
        return receiveMessage();
    }
}