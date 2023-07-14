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
                .forAddress("localhost", 9000)
                .maxInboundMessageSize(40 * 1024 * 1024)
                .usePlaintext()
                .build();
        run(100);
    }

    public static void run(int n) throws Exception {
        var stub = MessageServiceGrpc.newBlockingStub(channel);
        long start = System.nanoTime();
        for (int i=0; i<n; i++) {
            Requests.requestResponse(stub, "Hello from client");
        }
        long stop = System.nanoTime();
        System.out.println(((stop-start)/n) + "nanosecs/request");
    }
}
