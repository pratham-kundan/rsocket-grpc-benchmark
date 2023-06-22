package org.spr.CustomBenchmarks;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.spr.CustomBenchmarks.Brunch.BmTask;
import org.spr.protos.MessageDbServiceGrpc;
import org.spr.protos.MessageServiceGrpc;
import org.spr.protos.SizedMessageServiceGrpc;

public class BaseGrpcBenchMark implements BmTask {

    public SizedMessageServiceGrpc.SizedMessageServiceBlockingStub sizedMessageServiceStub;
    public MessageServiceGrpc.MessageServiceBlockingStub messageServiceStub;
    public MessageDbServiceGrpc.MessageDbServiceBlockingStub dbServiceStub;
    ManagedChannel channel;

    @Override
    public void run() {
    }

    @Override
    public void beforeAll() {
        channel = ManagedChannelBuilder
                .forAddress("localhost", 8787)
                .usePlaintext()
                .build();

        sizedMessageServiceStub = SizedMessageServiceGrpc.newBlockingStub(channel);

        messageServiceStub = MessageServiceGrpc.newBlockingStub(channel);

        dbServiceStub = MessageDbServiceGrpc.newBlockingStub(channel);
    }

    @Override
    public void afterAll() {
        channel.shutdown();
    }
}
