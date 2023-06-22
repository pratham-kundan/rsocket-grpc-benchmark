package org.spr;

import org.spr.components.RequestComponent;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class RSocketClient {
    public static void main(String[] args) throws Exception {
        ConfigurableApplicationContext context = SpringApplication.run(RSocketClient.class, args);

        RequestComponent requestFiringComponent = context.getBean(RequestComponent.class);
        requestFiringComponent.fireRequests();
    }
}
