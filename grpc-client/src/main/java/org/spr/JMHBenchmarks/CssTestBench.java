package org.spr.JMHBenchmarks;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.openjdk.jmh.annotations.*;
import org.spr.protos.MessageServiceGrpc;
import org.spr.protos.ProtoMessage;
import org.spr.requests.Requests;

/**
 * for Protobuf
 * This class contains functions to benchmark the throughput of
 * server functions returning echos.
 */
public class CssTestBench extends BaseTestBench {

    @Benchmark
    @Threads(10)
    public void benchmarkStreamResponseA(ExecutionPlan execPlan) throws InterruptedException {
        ProtoMessage response = Requests.streamResponse(execPlan.stub, "Hello from client");
    }


    @Benchmark
    @Threads(20)
    public void benchmarkStreamResponseB(ExecutionPlan execPlan) throws InterruptedException {
        ProtoMessage response = Requests.streamResponse(execPlan.stub, "Hello from client");
    }


    @Benchmark
    @Threads(50)
    public void benchmarkStreamResponseC(ExecutionPlan execPlan) throws InterruptedException {
        ProtoMessage response = Requests.streamResponse(execPlan.stub, "Hello from client");
    }

    @Benchmark
    @Threads(100)
    public void benchmarkStreamResponseD(ExecutionPlan execPlan) throws InterruptedException {
        ProtoMessage response = Requests.streamResponse(execPlan.stub, "Hello from client");
    }


    @State(Scope.Thread)
    public static class ExecutionPlan {
        public ManagedChannel channel;

        public MessageServiceGrpc.MessageServiceStub stub;

        @Setup(Level.Iteration)
        public void setUp() {
            channel = ManagedChannelBuilder
                    .forAddress(serverHost, serverPort)
                    .usePlaintext()
                    .build();

            stub = MessageServiceGrpc.newStub(channel);
        }

        @TearDown(Level.Iteration)
        public void tearDown() {
            channel.shutdown();
        }
    }

}
