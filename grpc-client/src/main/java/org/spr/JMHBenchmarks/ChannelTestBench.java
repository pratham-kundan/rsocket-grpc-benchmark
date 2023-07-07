package org.spr.JMHBenchmarks;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.openjdk.jmh.annotations.*;
import org.spr.protos.MessageServiceGrpc;
import org.spr.protos.ProtoMessage;
import org.spr.requests.Requests;

import java.util.List;

/**
 * This class contains functions to benchmark for bidirectional Streaming.
 */
public class ChannelTestBench extends BaseTestBench {

    @Benchmark
    @Threads(10)
    public void benchmarkChannelA(ExecutionPlan execPlan) throws InterruptedException {
        List<ProtoMessage> response = Requests.biStream(execPlan.bStub, "Hello from client");
    }

    @Benchmark
    @Threads(20)
    public void benchmarkChannelB(ExecutionPlan execPlan) throws InterruptedException {
        List<ProtoMessage> response = Requests.biStream(execPlan.bStub, "Hello from client");
    }

    @Benchmark
    @Threads(50)
    public void benchmarkChannelC(ExecutionPlan execPlan) throws InterruptedException {
        List<ProtoMessage> response = Requests.biStream(execPlan.bStub, "Hello from client");
    }


    @Benchmark
    @Threads(100)
    public void benchmarkChannelD(ExecutionPlan execPlan) throws InterruptedException {
        List<ProtoMessage> response = Requests.biStream(execPlan.bStub, "Hello from client");
    }


    @State(Scope.Thread)
    public static class ExecutionPlan {
        public ManagedChannel channel;

        public MessageServiceGrpc.MessageServiceStub bStub;

        @Setup(Level.Iteration)
        public void setUp() {
            channel = ManagedChannelBuilder
                    .forAddress(serverHost, serverPort)
                    .usePlaintext()
                    .build();

            bStub = MessageServiceGrpc.newStub(channel);
        }

        @TearDown(Level.Iteration)
        public void tearDown() {
            channel.shutdown();
        }
    }

}
