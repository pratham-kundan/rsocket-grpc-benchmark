package org.spr;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.spr.protos.MessageDbServiceGrpc;
import org.spr.protos.MessageServiceGrpc;
import org.spr.requests.Requests;

public class GrpcClient {
    static ManagedChannel channel;

    public static void main(String[] args) throws Exception {
        channel = ManagedChannelBuilder
                .forAddress("localhost", 8787)
                .maxInboundMessageSize(40 * 1024 * 1024)
                .usePlaintext()
                .build();
        run();
    }

    public static void run() throws Exception {
        var stub = MessageDbServiceGrpc.newBlockingStub(channel);
        Requests.getAllMessages(stub);
    }
}
