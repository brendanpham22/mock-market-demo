package com.ascertain.mockdemo.service;


import com.ascertain.mockdemo.entity.Product;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class RabbitMQConsumer {
    @RabbitListener(queues = {"${rabbitmq.queue.name}"})
    public void consume(List<Product> product){
        log.info(String.format("Received message -> %s", JSONObject.valueToString(product)));
    }
}