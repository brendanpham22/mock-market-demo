package com.ascertain.mockdemo.service;

import com.ascertain.mockdemo.entity.Product;
import com.ascertain.mockdemo.repo.ProductRepo;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
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
    public void syncData(List<Product> products){
        try {
            CompletableFuture.allOf(
                    CompletableFuture.runAsync(() -> updateProductList(products)),
                    CompletableFuture.runAsync(() -> pushProductList(products))
            ).get();
        } catch (InterruptedException e) {
            log.error(e.toString());
            Thread.currentThread().interrupt();
        } catch (ExecutionException e) {
            log.error(e.toString());
        }
    }

    public void updateProductList(List<Product> products){
        productRepo.saveAll(products);
    }

    public void pushProductList(List<Product> products){
        rabbitMQMessageProducer.publish(products);
    }
}
