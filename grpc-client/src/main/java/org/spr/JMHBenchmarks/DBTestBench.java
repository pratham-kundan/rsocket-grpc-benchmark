package org.spr.JMHBenchmarks;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.openjdk.jmh.annotations.*;
import org.spr.Requests;
import org.spr.protos.MessageDbServiceGrpc;
import org.spr.protos.ProtoMessage;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * for Protobuf
 * This class contains functions to benchmark the throughput of
 * database query endpoints
 */
@BenchmarkMode(Mode.Throughput)
@Fork(value = 2)
@Measurement(iterations = 3, time = 10, timeUnit = TimeUnit.SECONDS)
public class DBTestBench {
    @Benchmark
    @Fork(value = 1, warmups = 1)
    @BenchmarkMode(Mode.Throughput)
    @Warmup(iterations = 1)
    @Threads(10)
    public void benchmarkDbGetAllA(ExecutionPlan execPlan) {
        List<ProtoMessage> messageList = Requests.getAllMessages(execPlan.dbBStub);
    }

    @Benchmark
    @Fork(value = 1, warmups = 1)
    @BenchmarkMode(Mode.Throughput)
    @Warmup(iterations = 1)
    @Threads(20)
    public void benchmarkDbGetAllB(ExecutionPlan execPlan) {
        List<ProtoMessage> messageList = Requests.getAllMessages(execPlan.dbBStub);
    }

    @Benchmark
    @Fork(value = 1, warmups = 1)
    @BenchmarkMode(Mode.Throughput)
    @Warmup(iterations = 1)
    @Threads(50)
    public void benchmarkDbGetAllC(ExecutionPlan execPlan) {
        List<ProtoMessage> messageList = Requests.getAllMessages(execPlan.dbBStub);
    }

    @Benchmark
    @Fork(value = 1, warmups = 1)
    @BenchmarkMode(Mode.Throughput)
    @Warmup(iterations = 1)
    @Threads(100)
    public void benchmarkDbGetAllD(ExecutionPlan execPlan) {
        List<ProtoMessage> messageList = Requests.getAllMessages(execPlan.dbBStub);
    }

    @State(Scope.Thread)
    public static class ExecutionPlan {
        public ManagedChannel channel;

        public MessageDbServiceGrpc.MessageDbServiceBlockingStub dbBStub;

        @Setup(Level.Iteration)
        public void setUp() {
            channel = ManagedChannelBuilder
                    .forAddress("localhost", 8787)
                    .maxInboundMessageSize(40 * 1024 * 1024)
                    .usePlaintext()
                    .build();

            dbBStub = MessageDbServiceGrpc.newBlockingStub(channel);
        }

        @TearDown(Level.Iteration)
        public void tearDown() {
            channel.shutdown();
        }
    }

}

