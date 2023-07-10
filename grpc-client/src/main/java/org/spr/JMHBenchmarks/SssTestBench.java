package org.spr.JMHBenchmarks;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.openjdk.jmh.annotations.*;
import org.spr.protos.MessageServiceGrpc;
import org.spr.protos.ProtoMessage;
import org.spr.requests.Requests;

import java.util.List;

/**
 * This class contains functions to benchmark Server Streaming data
 * to the client
 */
public class SssTestBench extends BaseTestBench {
    @Benchmark
    @Threads(10)
    public void benchmarkRequestStreamA(ExecutionPlan execPlan) {
        List<ProtoMessage> response = Requests.requestStream(execPlan.bStub, "Hello from client");
    }

    @Benchmark
    @Threads(20)
    public void benchmarkRequestStreamB(ExecutionPlan execPlan) {
        List<ProtoMessage> response = Requests.requestStream(execPlan.bStub, "Hello from client");
    }

    @Benchmark
    @Threads(50)
    public void benchmarkRequestStreamC(ExecutionPlan execPlan) {
        List<ProtoMessage> response = Requests.requestStream(execPlan.bStub, "Hello from client");
    }

    @Benchmark
    @Threads(100)
    public void benchmarkRequestStreamD(ExecutionPlan execPlan) {
        List<ProtoMessage> response = Requests.requestStream(execPlan.bStub, "Hello from client");
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
