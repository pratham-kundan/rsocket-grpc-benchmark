package org.spr.JMHBenchmarks;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.openjdk.jmh.annotations.*;
import org.spr.protos.MessageDbServiceGrpc;
import org.spr.protos.ProtoMessage;
import org.spr.requests.Requests;
import org.spr.utils.Utils;

import java.util.List;
import java.util.stream.Collectors;

/**
 * This class contains benchmarks for Benchmarking Database service
 * It first adds several documents to the database and then removes all added documents
 * <p>
 * Both were done simultaneously to prevent storing too much data on the database
 */
public class DbBiStreamTestBench extends BaseTestBench {

    @Benchmark
    @Threads(10)
    public void benchmarkDbPushRemoveA(ExecutionPlan execPlan) throws InterruptedException {
        List<ProtoMessage> messageList = Requests.pushAll(execPlan.dbStub, Utils.generateRandomString(100, 100));
        List<String> toBeDeleted = messageList.stream().map(ProtoMessage::getId).collect(Collectors.toList());
        ProtoMessage response = Requests.removeAll(execPlan.dbStub, toBeDeleted);
    }


    @Benchmark
    @Threads(20)
    public void benchmarkDbPushRemoveB(ExecutionPlan execPlan) throws InterruptedException {
        List<ProtoMessage> messageList = Requests.pushAll(execPlan.dbStub, Utils.generateRandomString(100, 100));
        List<String> toBeDeleted = messageList.stream().map(ProtoMessage::getId).collect(Collectors.toList());
        ProtoMessage response = Requests.removeAll(execPlan.dbStub, toBeDeleted);
    }

    @Benchmark
    @Threads(50)
    public void benchmarkPushRemoveC(ExecutionPlan execPlan) throws InterruptedException {
        List<ProtoMessage> messageList = Requests.pushAll(execPlan.dbStub, Utils.generateRandomString(100, 100));
        List<String> toBeDeleted = messageList.stream().map(ProtoMessage::getId).collect(Collectors.toList());
        ProtoMessage response = Requests.removeAll(execPlan.dbStub, toBeDeleted);
    }

    @Benchmark
    @Threads(100)
    public void benchmarkPushRemoveD(ExecutionPlan execPlan) throws InterruptedException {
        List<ProtoMessage> messageList = Requests.pushAll(execPlan.dbStub, Utils.generateRandomString(100, 100));
        List<String> toBeDeleted = messageList.stream().map(ProtoMessage::getId).collect(Collectors.toList());
        ProtoMessage response = Requests.removeAll(execPlan.dbStub, toBeDeleted);
    }

    @State(Scope.Thread)
    public static class ExecutionPlan {
        public ManagedChannel channel;

        public MessageDbServiceGrpc.MessageDbServiceStub dbStub;

        @Setup(Level.Iteration)
        public void setUp() {
            channel = ManagedChannelBuilder
                    .forAddress(serverHost, serverPort)
                    .maxInboundMessageSize(40 * 1024 * 1024)
                    .usePlaintext()
                    .build();

            dbStub = MessageDbServiceGrpc.newStub(channel);
        }

        @TearDown(Level.Iteration)
        public void tearDown() {
            channel.shutdown();
        }
    }

}

