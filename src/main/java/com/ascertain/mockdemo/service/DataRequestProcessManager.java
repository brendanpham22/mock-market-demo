package com.ascertain.mockdemo.service;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.concurrent.*;

@Getter
@Component
public class DataRequestProcessManager {

    @Value("${server.pool.size}")
    private int poolSize;

    private ExecutorService executorService;

    @SuppressWarnings("java:S1452")
    public Future<?> addProcess(Runnable command){
        return getScheduledExecutorService().submit(command);
    }

    public ExecutorService getScheduledExecutorService(){
        if(executorService == null){
            executorService = Executors.newScheduledThreadPool(poolSize);
        }
        return executorService;
    }
}
