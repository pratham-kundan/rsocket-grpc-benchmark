package org.spr.JMHBenchmarks;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.openjdk.jmh.annotations.*;
import org.spr.protos.MessageServiceGrpc;
import org.spr.protos.ProtoMessage;
import org.spr.requests.Requests;

/**
 * This class contains functions to benchmark the throughput of
 * server functions returning echos.
 */
public class ReqResTestBench extends BaseTestBench {

    @Benchmark
    @Threads(10)
    public void benchmarkRequestResponseA(ExecutionPlan execPlan) {
        ProtoMessage response = Requests.requestResponse(execPlan.bStub, "Hello from client");
    }

    @Benchmark
    @Threads(20)
    public void benchmarkRequestResponseB(ExecutionPlan execPlan) {
        ProtoMessage response = Requests.requestResponse(execPlan.bStub, "Hello from client");
    }

    @Benchmark
    @Threads(50)
    public void benchmarkRequestResponseC(ExecutionPlan execPlan) {
        ProtoMessage response = Requests.requestResponse(execPlan.bStub, "Hello from client");
    }

    @Benchmark
    @Threads(100)
    public void benchmarkRequestResponseD(ExecutionPlan execPlan) {
        ProtoMessage response = Requests.requestResponse(execPlan.bStub, "Hello from client");
    }

    @State(Scope.Thread)
    public static class ExecutionPlan {
        public ManagedChannel channel;

        public MessageServiceGrpc.MessageServiceBlockingStub bStub;

        @Setup(Level.Iteration)
        public void setUp() {
            channel = ManagedChannelBuilder
                    .forAddress(serverHost, serverPort)
                    .usePlaintext()
                    .build();

            bStub = MessageServiceGrpc.newBlockingStub(channel);
        }

        @TearDown(Level.Iteration)
        public void tearDown() {
            channel.shutdown();
        }
    }

}
