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
@BenchmarkMode(Mode.Throughput)
@Fork(value = 2)
@Measurement(iterations = 3, time = 10, timeUnit = TimeUnit.SECONDS)
public class CssTestBench {

    @Benchmark
    @Fork(value = 1, warmups = 1)
    @BenchmarkMode(Mode.Throughput)
    @Warmup(iterations = 1)
    @Threads(10)
    public void benchmarkStreamResponseT10(ExecutionPlan execPlan) throws InterruptedException {
        ProtoMessage response = Requests.streamResponse(execPlan.bStub, "Hello from client");
    }

    @Benchmark
    @Fork(value = 1, warmups = 1)
    @BenchmarkMode(Mode.Throughput)
    @Warmup(iterations = 1)
    @Threads(20)
    public void benchmarkStreamResponseT20(ExecutionPlan execPlan) throws InterruptedException {
        ProtoMessage response = Requests.streamResponse(execPlan.bStub, "Hello from client");
    }

    @Benchmark
    @Fork(value = 1, warmups = 1)
    @BenchmarkMode(Mode.Throughput)
    @Warmup(iterations = 1)
    @Threads(10)
    public void benchmarkBiStreamT10(ExecutionPlan execPlan) throws InterruptedException {
        List<ProtoMessage> response = Requests.biStream(execPlan.bStub, "Hello from client");
    }

    @Benchmark
    @Fork(value = 1, warmups = 1)
    @BenchmarkMode(Mode.Throughput)
    @Warmup(iterations = 1)
    @Threads(20)
    public void benchmarkBiStreamT20(ExecutionPlan execPlan) throws InterruptedException {
        List<ProtoMessage> response = Requests.biStream(execPlan.bStub, "Hello from client");
    }

    @State(Scope.Thread)
    public static class ExecutionPlan {
        public ManagedChannel channel;

        public MessageServiceGrpc.MessageServiceStub bStub;

        @Setup(Level.Iteration)
        public void setUp() {
            channel = ManagedChannelBuilder
                    .forAddress("localhost", 8787)
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
