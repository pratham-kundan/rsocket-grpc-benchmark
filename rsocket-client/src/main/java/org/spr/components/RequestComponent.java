package org.spr.components;

import org.spr.protos.ProtoMessage;
import org.spr.requests.ProtoRequests;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.rsocket.RSocketRequester;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

/**
 * Component to help with firing requests for testing
 */
@Component
public class RequestComponent {
    @Autowired
    private final RSocketRequester rSocketRequester;

    public RequestComponent(RSocketRequester rSocketRequester) {
        this.rSocketRequester = rSocketRequester;
    }

    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";

    public static String generateRandomString(int length) {
        Random random = new Random();
        StringBuilder sb = new StringBuilder(length);

        for (int i = 0; i < length; i++) {
            int randomIndex = random.nextInt(CHARACTERS.length());
            char randomChar = CHARACTERS.charAt(randomIndex);
            sb.append(randomChar);
        }

        return sb.toString();
    }

    public void fireRequests(int n) {
        long start = System.nanoTime();
        for (int i=0; i<n; i++) {
            ProtoRequests.requestResponse(rSocketRequester, "Hello from client");
        }
        long stop = System.nanoTime();
        System.out.println(((stop - start)/(n*1e6)) + "ms/request");

    }
}
