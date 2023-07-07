package org.spr.JMHBenchmarks;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.openjdk.jmh.annotations.*;
import org.spr.protos.MessageDbServiceGrpc;
import org.spr.protos.ProtoMessage;
import org.spr.requests.Requests;

import java.util.List;

/**
 * for Protobuf
 * This class contains functions to benchmark the throughput of
 * database query endpoints
 */
public class DBTestBench extends BaseTestBench {

    @Benchmark
    @Threads(10)
    public void benchmarkDbGetAllA(ExecutionPlan execPlan) {
        List<ProtoMessage> messageList = Requests.getAllMessages(execPlan.dbBStub);
    }

    @Benchmark
    @Threads(20)
    public void benchmarkDbGetAllB(ExecutionPlan execPlan) {
        List<ProtoMessage> messageList = Requests.getAllMessages(execPlan.dbBStub);
    }

    @Benchmark
    @Threads(50)
    public void benchmarkDbGetAllC(ExecutionPlan execPlan) {
        List<ProtoMessage> messageList = Requests.getAllMessages(execPlan.dbBStub);
    }

    @Benchmark
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
                    .forAddress(serverHost, serverPort)
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

