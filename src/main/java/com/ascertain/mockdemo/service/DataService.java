package com.ascertain.mockdemo.service;

import com.ascertain.mockdemo.entity.Product;
import com.ascertain.mockdemo.repo.ProductRepo;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
@Slf4j
@Getter
@Service
@AllArgsConstructor
public class DataService {
    private final RabbitMQMessageProducer rabbitMQMessageProducer;
    private final ProductRepo productRepo;
    private final SimpMessagingTemplate simpMessagingTemplate;
    public void syncData(List<Product> products){
        try {
            CompletableFuture.allOf(
                    CompletableFuture.runAsync(() -> updateProductList(products)),
                    CompletableFuture.runAsync(() -> pushProductList(products)),
                    CompletableFuture.runAsync(() -> reply(products))
            ).get();
        } catch (InterruptedException e) {
            log.error(e.toString());
            Thread.currentThread().interrupt();
        } catch (ExecutionException e) {
            log.error(e.toString());
        }
    }

    public void updateProductList(List<Product> products){
        List<Product> productList = productRepo.findAll();
        List<Product> saveList = products;
        saveList.removeAll(productList);
        productRepo.saveAll(saveList);
    }

    public void pushProductList(List<Product> products){
        rabbitMQMessageProducer.publish(products);
    }

    public void reply(List<Product> products) {
        this.simpMessagingTemplate.convertAndSend("/topic/products", products);
    }
}
