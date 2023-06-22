package org.spr.JmhBenchmarks;

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
 * database query endpoints
 */
@BenchmarkMode(Mode.Throughput)
@Fork(value = 2)
public class DBTestBench {
    @Benchmark
    @Fork(value = 1, warmups = 1)
    @BenchmarkMode(Mode.Throughput)
    @Warmup(iterations = 1)
    @Threads(10)
    public void benchmarkDbGetAllA(ExecutionPlan execPlan) {
        List<SimpleMessage> messageList = Requests.getAllMessages(execPlan.dbBStub);
    }

    @Benchmark
    @Fork(value = 1, warmups = 1)
    @BenchmarkMode(Mode.Throughput)
    @Warmup(iterations = 1)
    @Threads(20)
    public void benchmarkDbGetAllB(ExecutionPlan execPlan) {
        List<SimpleMessage> messageList = Requests.getAllMessages(execPlan.dbBStub);
    }

    @Benchmark
    @Fork(value = 1, warmups = 1)
    @BenchmarkMode(Mode.Throughput)
    @Warmup(iterations = 1)
    @Threads(50)
    public void benchmarkDbGetAllC(ExecutionPlan execPlan) {
        List<SimpleMessage> messageList = Requests.getAllMessages(execPlan.dbBStub);
    }

    @Benchmark
    @Fork(value = 1, warmups = 1)
    @BenchmarkMode(Mode.Throughput)
    @Warmup(iterations = 1)
    @Threads(100)
    public void benchmarkDbGetAllD(ExecutionPlan execPlan) {
        List<SimpleMessage> messageList = Requests.getAllMessages(execPlan.dbBStub);
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

