package org.spr;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.spr.protos.MessageServiceGrpc;
import org.spr.protos.SimpleMessage;

public class GrpcPingClient {

    public static void main(String[] args) {
        String host = "localhost";
        int port = 9090;

        ManagedChannel channel = ManagedChannelBuilder.forAddress(host, port)
                .usePlaintext()
                .build();

        MessageServiceGrpc.MessageServiceBlockingStub blockingStub = MessageServiceGrpc.newBlockingStub(channel);

        SimpleMessage message = SimpleMessage
                .newBuilder()
                .setBody("Hello everyone")
                .build();

        MessageServiceGrpc.MessageServiceBlockingStub stub = MessageServiceGrpc.newBlockingStub(channel);

        SimpleMessage response = blockingStub.ping(message);

        System.out.println("Response from server: " + response.getBody());

        stub.getAll(message).forEachRemaining(System.out::println);
    }
}
