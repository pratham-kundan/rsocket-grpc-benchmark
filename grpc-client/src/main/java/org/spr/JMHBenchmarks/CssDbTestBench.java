package org.spr.JMHBenchmarks;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.openjdk.jmh.annotations.*;
import org.spr.protos.MessageDbServiceGrpc;
import org.spr.protos.ProtoMessage;
import org.spr.requests.Requests;
import org.spr.utils.Utils;

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
public class CssDbTestBench {


    @Benchmark
    @Fork(value = 1, warmups = 1)
    @BenchmarkMode(Mode.Throughput)
    @Warmup(iterations = 1)
    @Threads(10)
    public void benchmarkDbPushRemoveA(ExecutionPlan execPlan) throws InterruptedException {
        List<ProtoMessage> messageList = Requests.pushAll(execPlan.dbBStub, Utils.generateRandomString(100, 100));
        List<String> toBeDeleted = messageList.stream().map(ProtoMessage::getId).toList();
        ProtoMessage response = Requests.removeAll(execPlan.dbBStub, toBeDeleted);
    }

    @Benchmark
    @Fork(value = 1, warmups = 1)
    @BenchmarkMode(Mode.Throughput)
    @Warmup(iterations = 1)
    @Threads(20)
    public void benchmarkDbPushRemoveB(ExecutionPlan execPlan) throws InterruptedException {
        List<ProtoMessage> messageList = Requests.pushAll(execPlan.dbBStub, Utils.generateRandomString(100, 100));
        List<String> toBeDeleted = messageList.stream().map(ProtoMessage::getId).toList();
        ProtoMessage response = Requests.removeAll(execPlan.dbBStub, toBeDeleted);
    }

    @Benchmark
    @Fork(value = 1, warmups = 1)
    @BenchmarkMode(Mode.Throughput)
    @Warmup(iterations = 1)
    @Threads(50)
    public void benchmarkPushRemoveC(ExecutionPlan execPlan) throws InterruptedException {
        List<ProtoMessage> messageList = Requests.pushAll(execPlan.dbBStub, Utils.generateRandomString(100, 100));
        List<String> toBeDeleted = messageList.stream().map(ProtoMessage::getId).toList();
        ProtoMessage response = Requests.removeAll(execPlan.dbBStub, toBeDeleted);
    }

    @Benchmark
    @Fork(value = 1, warmups = 1)
    @BenchmarkMode(Mode.Throughput)
    @Warmup(iterations = 1)
    @Threads(100)
    public void benchmarkPushRemoveD(ExecutionPlan execPlan) throws InterruptedException {
        List<ProtoMessage> messageList = Requests.pushAll(execPlan.dbBStub, Utils.generateRandomString(100, 100));
        List<String> toBeDeleted = messageList.stream().map(ProtoMessage::getId).toList();
        ProtoMessage response = Requests.removeAll(execPlan.dbBStub, toBeDeleted);
    }

    @State(Scope.Thread)
    public static class ExecutionPlan {
        public ManagedChannel channel;

        public MessageDbServiceGrpc.MessageDbServiceStub dbBStub;

        @Setup(Level.Iteration)
        public void setUp() {
            channel = ManagedChannelBuilder
                    .forAddress("localhost", 8787)
                    .maxInboundMessageSize(40 * 1024 * 1024)
                    .usePlaintext()
                    .build();

            dbBStub = MessageDbServiceGrpc.newStub(channel);
        }

        @TearDown(Level.Iteration)
        public void tearDown() {
            channel.shutdown();
        }
    }

}

