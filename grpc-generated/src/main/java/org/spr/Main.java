package org.spr;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.spr.protos.*;

import java.util.ArrayList;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        String host = "localhost";
        int port = 8787;

        ManagedChannel channel = ManagedChannelBuilder.forAddress(host, port).usePlaintext().build();

        MessageServiceGrpc.MessageServiceBlockingStub stub = MessageServiceGrpc.newBlockingStub(channel);

        SimpleMessage message = SimpleMessage.newBuilder().setBody("Hello everyone").build();

        SimpleMessage response = stub.ping(message);

        MessageDbServiceGrpc.MessageDbServiceBlockingStub dbBStub = MessageDbServiceGrpc.newBlockingStub(channel);
        List<SimpleMessage> simpleMessageList = new ArrayList<>();
        dbBStub.getAll(SimpleMessage.newBuilder().build()).forEachRemaining(simpleMessageList::add);

        System.out.println("Response from server: " + simpleMessageList);

        SizedMessageServiceGrpc.SizedMessageServiceBlockingStub smsBStub = SizedMessageServiceGrpc.newBlockingStub(channel);

        SimpleSizedMessage smResponse = smsBStub.ping(SizedMessageRequest.newBuilder().setSizeMb(3).build());

        var data = smResponse.getDataList();
        System.out.println(data.size());
        System.out.println(smResponse.getSerializedSize());

        List<SimpleSizedMessage> smStreamResponse = new ArrayList<>();
        smsBStub.echoStream(SizedMessageRequest.newBuilder().setSizeMb(10).build()).forEachRemaining(smStreamResponse::add);

        System.out.println(smStreamResponse.size());
        System.out.println(smStreamResponse.get(0).getSerializedSize() / 1024);
    }
}
