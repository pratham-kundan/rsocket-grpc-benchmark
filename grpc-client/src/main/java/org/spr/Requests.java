package org.spr;

import org.spr.protos.*;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class Requests {
    public static ProtoMessage requestResponse(MessageServiceGrpc.MessageServiceBlockingStub stub, String requestText) {
        ProtoMessage message = ProtoMessage
                .newBuilder()
                .setBody("Hello from client " + Instant.now())
                .build();

        return stub.ping(message);
    }

    public static List<ProtoMessage> requestStream(MessageServiceGrpc.MessageServiceBlockingStub stub, String requestText) {
        ProtoMessage message = ProtoMessage
                .newBuilder()
                .setBody("Hello from client " + Instant.now())
                .build();
        List<ProtoMessage> protoMessageList = new ArrayList<>();

        stub.echoStream(message)
                .forEachRemaining(protoMessageList::add);

        return protoMessageList;
    }

    public static List<ProtoMessage> getAllMessages(MessageDbServiceGrpc.MessageDbServiceBlockingStub stub) {
        List<ProtoMessage> protoMessageList = new ArrayList<>();

        stub
                .getAll(ProtoMessage.newBuilder().build())
                .forEachRemaining(protoMessageList::add);

        return protoMessageList;
    }

    public static ProtoMessage createMessage(MessageDbServiceGrpc.MessageDbServiceBlockingStub stub, ProtoMessage request) {
        return stub.create(request);
    }

    public static ProtoSizedMessage sizedRequestResponse(SizedMessageServiceGrpc.SizedMessageServiceBlockingStub stub, int sizeMb) {
        return stub.ping(SizedMessageRequest
                .newBuilder()
                .setSizeMb(sizeMb)
                .build()
        );
    }

    public static List<ProtoSizedMessage> sizedRequestStream(SizedMessageServiceGrpc.SizedMessageServiceBlockingStub stub, int sizeMb) {
        List<ProtoSizedMessage> response = new ArrayList<>();
        stub.echoStream(SizedMessageRequest
                .newBuilder()
                .setSizeMb(sizeMb)
                .build()
        ).forEachRemaining(response::add);

        return response;
    }
}
