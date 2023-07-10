package org.spr.JMHBenchmarks;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.openjdk.jmh.annotations.*;
import org.spr.protos.MessageDbServiceGrpc;
import org.spr.protos.ProtoMessage;
import org.spr.requests.Requests;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

/**
 * This class contains functions to benchmark the request-response
 * to the database
 */
public class DbReqResTestBench extends BaseTestBench {

    @Benchmark
    @Threads(10)
    public void benchmarkDbGetOneA(ExecutionPlan execPlan) {
        String message = execPlan.presentMessages.get(execPlan.random.nextInt(0, execPlan.presentMessages.size()));
        ProtoMessage response = Requests.getById(execPlan.dbBStub, message);
    }

    @Benchmark
    @Threads(20)
    public void benchmarkDbGetOneB(ExecutionPlan execPlan) {
        String message = execPlan.presentMessages.get(execPlan.random.nextInt(0, execPlan.presentMessages.size()));
        ProtoMessage response = Requests.getById(execPlan.dbBStub, message);
    }

    @Benchmark
    @Threads(50)
    public void benchmarkDbGetOneC(ExecutionPlan execPlan) {
        String message = execPlan.presentMessages.get(execPlan.random.nextInt(0, execPlan.presentMessages.size()));
        ProtoMessage response = Requests.getById(execPlan.dbBStub, message);
    }

    @Benchmark
    @Threads(100)
    public void benchmarkDbGetOneD(ExecutionPlan execPlan) {
        String message = execPlan.presentMessages.get(execPlan.random.nextInt(0, execPlan.presentMessages.size()));
        ProtoMessage response = Requests.getById(execPlan.dbBStub, message);
    }

    @State(Scope.Thread)
    public static class ExecutionPlan {
        public ManagedChannel channel;
        public Random random = new Random(1234);
        public MessageDbServiceGrpc.MessageDbServiceBlockingStub dbBStub;
        public List<String> presentMessages;

        @Setup(Level.Iteration)
        public void setUp() throws InterruptedException {
            channel = ManagedChannelBuilder
                    .forAddress(serverHost, serverPort)
                    .maxInboundMessageSize(40 * 1024 * 1024)
                    .usePlaintext()
                    .build();

            dbBStub = MessageDbServiceGrpc.newBlockingStub(channel);

            presentMessages = Requests.getAllMessages(dbBStub)
                    .stream().map(ProtoMessage::getId).collect(Collectors.toList());
        }

        @TearDown(Level.Iteration)
        public void tearDown() throws InterruptedException {
            channel.shutdown();
        }
    }

}

