package org.spr;

import org.spr.protos.*;

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

    public static SimpleSizedMessage sizedRequestResponse(SizedMessageServiceGrpc.SizedMessageServiceBlockingStub stub, int sizeMb) {
        return stub.ping(SizedMessageRequest
                .newBuilder()
                .setSizeMb(sizeMb)
                .build()
        );
    }

    public static List<SimpleSizedMessage> sizedRequestStream(SizedMessageServiceGrpc.SizedMessageServiceBlockingStub stub, int sizeMb) {
        List<SimpleSizedMessage> response = new ArrayList<>();
        stub.echoStream(SizedMessageRequest
                .newBuilder()
                .setSizeMb(sizeMb)
                .build()
        ).forEachRemaining(response::add);

        return response;
    }
}
