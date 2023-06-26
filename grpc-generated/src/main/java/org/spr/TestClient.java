package org.spr;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.spr.protos.*;

import java.util.ArrayList;
import java.util.List;

public class TestClient {

    public static void main(String[] args) {
        String host = "localhost";
        int port = 8787;

        ManagedChannel channel = ManagedChannelBuilder.forAddress(host, port).usePlaintext().build();

        MessageServiceGrpc.MessageServiceBlockingStub stub = MessageServiceGrpc.newBlockingStub(channel);

        ProtoMessage message = ProtoMessage.newBuilder().setBody("Hello everyone").build();

        ProtoMessage response = stub.ping(message);

        MessageDbServiceGrpc.MessageDbServiceBlockingStub dbBStub = MessageDbServiceGrpc.newBlockingStub(channel);
        List<ProtoMessage> protoMessageList = new ArrayList<>();
        dbBStub.getAll(ProtoMessage.newBuilder().build()).forEachRemaining(protoMessageList::add);

        System.out.println("Response from server: " + protoMessageList);

        SizedMessageServiceGrpc.SizedMessageServiceBlockingStub smsBStub = SizedMessageServiceGrpc.newBlockingStub(channel);

        ProtoSizedMessage smResponse = smsBStub.ping(SizedMessageRequest.newBuilder().setSizeMb(3).build());

        var data = smResponse.getDataList();
        System.out.println(data.size());
        System.out.println(smResponse.getSerializedSize());

        List<ProtoSizedMessage> smStreamResponse = new ArrayList<>();
        smsBStub.echoStream(SizedMessageRequest.newBuilder().setSizeMb(10).build()).forEachRemaining(smStreamResponse::add);

        System.out.println(smStreamResponse.size());
        System.out.println(smStreamResponse.get(0).getSerializedSize() / 1024);
    }
}
