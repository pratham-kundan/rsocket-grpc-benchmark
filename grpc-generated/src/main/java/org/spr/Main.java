package org.spr;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.spr.protos.MessageDbServiceGrpc;
import org.spr.protos.MessageServiceGrpc;
import org.spr.protos.SimpleMessage;

import java.util.ArrayList;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        String host = "localhost";
        int port = 8787;

        ManagedChannel channel = ManagedChannelBuilder.forAddress(host, port)
                .usePlaintext()
                .build();

        MessageServiceGrpc.MessageServiceBlockingStub stub = MessageServiceGrpc.newBlockingStub(channel);

        SimpleMessage message = SimpleMessage
                .newBuilder()
                .setBody("Hello everyone")
                .build();

        SimpleMessage response = stub.ping(message);

        MessageDbServiceGrpc.MessageDbServiceBlockingStub bStub = MessageDbServiceGrpc.newBlockingStub(channel);
        List<SimpleMessage> simpleMessageList = new ArrayList<>();
        bStub
                .getAll(SimpleMessage.newBuilder().build())
                .forEachRemaining(simpleMessageList::add);

        System.out.println("Response from server: " + simpleMessageList);
    }
}
