package org.spr.JMHBenchmarks;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.openjdk.jmh.annotations.*;
import org.spr.requests.Requests;
import org.spr.protos.MessageServiceGrpc;
import org.spr.protos.ProtoMessage;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * for Protobuf
 * This class contains functions to benchmark the throughput of
 * server functions returning echos.
 */
@Fork(value = 1, warmups = 1)
@BenchmarkMode(Mode.Throughput)
@Warmup(iterations = 1)
@Measurement(iterations = 3, time = 10, timeUnit = TimeUnit.SECONDS)
public class ReqResTestBench {

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
                    .forAddress("localhost", 8787)
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
