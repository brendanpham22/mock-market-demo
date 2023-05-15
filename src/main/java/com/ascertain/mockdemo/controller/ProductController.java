package com.ascertain.mockdemo.controller;

import com.ascertain.mockdemo.entity.Product;
import com.ascertain.mockdemo.repo.ProductRepo;
import com.ascertain.mockdemo.service.RabbitMQConsumer;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/products")
@AllArgsConstructor
public class ProductController {
    private RabbitMQConsumer queueConsumer;
    private ProductRepo productRepo;

    @GetMapping
    public ResponseEntity<?> getProducts() {
        List<Product> productList = queueConsumer.processMessage();
        return new ResponseEntity<>(productList, HttpStatus.OK);
    }

    @MessageMapping("/products")
    @SendTo("/topic/products")
    public List<Product> broadcastProducts(@Payload String message) {
        return queueConsumer.processMessage();
    }
}
