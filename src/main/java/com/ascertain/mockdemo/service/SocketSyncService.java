package com.ascertain.mockdemo.service;

import com.ascertain.mockdemo.entity.Product;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandler;
import org.springframework.stereotype.Service;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Future;

@Service
@Slf4j
public class SocketSyncService {
    @Value("${data.websockets.uri}")
    String websocketsUri;

    private final WebsocketsService websocketsService;

    public SocketSyncService(WebsocketsService websocketsService) {
        this.websocketsService = websocketsService;
    }

    private DataRequestProcessManager processManager;

    private final Map<String, StompSession> sessions = new ConcurrentHashMap<>();


    public void triggerSync(List<Product> products){
        websocketsService.connect(websocketsUri, new StompSessionHandler() {

            private Future<?> future;

            @Override
            public void afterConnected(StompSession session, StompHeaders connectedHeaders) {
                session.subscribe("/topic/data/products", this);
                future = processManager.addProcess(() -> session.send("/ws/data/products", products));
                sessions.put("products", session);
            }

            @SneakyThrows
            @Override
            public void handleException(StompSession stompSession, StompCommand stompCommand, StompHeaders stompHeaders, byte[] bytes, Throwable throwable) {
                log.error(String.valueOf(throwable));
            }

            @SneakyThrows
            @Override
            public void handleTransportError(StompSession stompSession, Throwable throwable) {
                log.error(String.valueOf(throwable));
            }

            @Override
            public Type getPayloadType(StompHeaders stompHeaders) {
                log.info("Headers {}", stompHeaders);
                return String.class;
            }

            @SneakyThrows
            @Override
            public void handleFrame(StompHeaders stompHeaders, Object payload) {
                log.info("Msg {}", payload);
                sessions.remove("products").disconnect();
                future.cancel(true);

            }
        });
    }

    private DataRequestProcessManager getProcessManager() {
        if (processManager == null) {
            processManager = new DataRequestProcessManager();
        }
        return processManager;
    }

}
