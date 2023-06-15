package org.spr;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.openjdk.jmh.annotations.*;
import org.spr.protos.MessageDbServiceGrpc;
import org.spr.protos.MessageServiceGrpc;
import org.spr.protos.SimpleMessage;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@BenchmarkMode(Mode.Throughput)
@Fork(value = 2)
@Threads(2)
public class TestBench {

    @Benchmark
    @Fork(value = 1, warmups = 1)
    @BenchmarkMode(Mode.Throughput)
    @Warmup(iterations = 1)
    public void benchmarkRequestResponse(ExecutionPlan execPlan) {
        MessageServiceGrpc.MessageServiceBlockingStub bStub = MessageServiceGrpc.newBlockingStub(execPlan.channel);

        SimpleMessage message = SimpleMessage
                .newBuilder()
                .setBody("Hello from client " + Instant.now())
                .build();

        SimpleMessage response = bStub.ping(message);
    }

    @Benchmark
    @Fork(value = 1, warmups = 1)
    @BenchmarkMode(Mode.Throughput)
    @Warmup(iterations = 1)
    public void benchmarkRequestStream(ExecutionPlan execPlan) {
        MessageServiceGrpc.MessageServiceBlockingStub bStub = MessageServiceGrpc.newBlockingStub(execPlan.channel);

        SimpleMessage message = SimpleMessage
                .newBuilder()
                .setBody("Hello from client " + Instant.now())
                .build();
        List<SimpleMessage> simpleMessageList = new ArrayList<>();

        bStub
                .echoStream(message)
                .forEachRemaining(simpleMessageList::add);
    }

    @Benchmark
    @Fork(value = 1, warmups = 1)
    @BenchmarkMode(Mode.Throughput)
    @Warmup(iterations = 1)
    public void benchmarkDbGetAll(ExecutionPlan execPlan) {
        MessageDbServiceGrpc.MessageDbServiceBlockingStub bStub = MessageDbServiceGrpc.newBlockingStub(execPlan.channel);
        List<SimpleMessage> simpleMessageList = new ArrayList<>();

        bStub
                .getAll(SimpleMessage.newBuilder().build())
                .forEachRemaining(simpleMessageList::add);
    }

    @State(Scope.Thread)
    public static class ExecutionPlan {
        public ManagedChannel channel = ManagedChannelBuilder
                .forAddress("localhost", 8787)
                .usePlaintext()
                .build();

        @Setup(Level.Invocation)
        public void setUp() {
        }
    }

}
