package org.spr.JMHBenchmarks;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.openjdk.jmh.annotations.*;
import org.spr.Requests;
import org.spr.protos.MessageDbServiceGrpc;
import org.spr.protos.MessageServiceGrpc;
import org.spr.protos.SimpleMessage;

import java.util.List;

/**
 * for Protobuf
 * This class contains functions to benchmark the throughput of
 * server functions returning echos.
 */
@BenchmarkMode(Mode.Throughput)
@Fork(value = 2)
@Threads(100)
public class TestBench {

    @Benchmark
    @Fork(value = 1, warmups = 1)
    @BenchmarkMode(Mode.Throughput)
    @Warmup(iterations = 1)
    @Threads(10)
    public void benchmarkRequestResponseT10(ExecutionPlan execPlan) {
        SimpleMessage response = Requests.requestResponse(execPlan.bStub, "Hello from client");
    }

    @Benchmark
    @Fork(value = 1, warmups = 1)
    @BenchmarkMode(Mode.Throughput)
    @Warmup(iterations = 1)
    @Threads(20)
    public void benchmarkRequestResponseT20(ExecutionPlan execPlan) {
        SimpleMessage response = Requests.requestResponse(execPlan.bStub, "Hello from client");
    }

    @Benchmark
    @Fork(value = 1, warmups = 1)
    @BenchmarkMode(Mode.Throughput)
    @Warmup(iterations = 1)
    @Threads(10)
    public void benchmarkRequestStreamT10(ExecutionPlan execPlan) {
        List<SimpleMessage> response = Requests.requestStream(execPlan.bStub, "Hello from client");
    }

    @Benchmark
    @Fork(value = 1, warmups = 1)
    @BenchmarkMode(Mode.Throughput)
    @Warmup(iterations = 1)
    @Threads(20)
    public void benchmarkRequestStreamT20(ExecutionPlan execPlan) {
        List<SimpleMessage> response = Requests.requestStream(execPlan.bStub, "Hello from client");
    }

    @State(Scope.Thread)
    public static class ExecutionPlan {
        public ManagedChannel channel = ManagedChannelBuilder
                .forAddress("localhost", 8787)
                .usePlaintext()
                .build();

        public MessageServiceGrpc.MessageServiceBlockingStub bStub;
        public MessageDbServiceGrpc.MessageDbServiceBlockingStub dbBStub;

        @Setup(Level.Invocation)
        public void setUp() {
            bStub = MessageServiceGrpc.newBlockingStub(channel);
            dbBStub = MessageDbServiceGrpc.newBlockingStub(channel);
        }
    }

}
