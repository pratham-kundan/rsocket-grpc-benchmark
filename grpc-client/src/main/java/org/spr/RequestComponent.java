package org.spr;

import org.spr.protos.MessageDbServiceGrpc;
import org.spr.protos.MessageServiceGrpc;
import org.spr.protos.SimpleMessage;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class RequestComponent {
    public static SimpleMessage requestResponse(MessageServiceGrpc.MessageServiceBlockingStub stub, String requestText) {
        SimpleMessage message = SimpleMessage
                .newBuilder()
                .setBody("Hello from client " + Instant.now())
                .build();

        return stub.ping(message);
    }

    public static List<SimpleMessage> requestStream(MessageServiceGrpc.MessageServiceBlockingStub stub, String requestText) {
        SimpleMessage message = SimpleMessage
                .newBuilder()
                .setBody("Hello from client " + Instant.now())
                .build();
        List<SimpleMessage> simpleMessageList = new ArrayList<>();

        stub.echoStream(message)
                .forEachRemaining(simpleMessageList::add);

        return simpleMessageList;
    }

    public static List<SimpleMessage> getAllMessages(MessageDbServiceGrpc.MessageDbServiceBlockingStub stub) {
        List<SimpleMessage> simpleMessageList = new ArrayList<>();

        stub
                .getAll(SimpleMessage.newBuilder().build())
                .forEachRemaining(simpleMessageList::add);

        return simpleMessageList;
    }

    public static SimpleMessage createMessage(MessageDbServiceGrpc.MessageDbServiceBlockingStub stub, SimpleMessage request) {
        return stub.create(request);
    }
}
