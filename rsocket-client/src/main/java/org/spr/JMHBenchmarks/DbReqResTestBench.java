package org.spr.JMHBenchmarks;

import org.openjdk.jmh.annotations.*;
import org.spr.CustomBenchmarks.RequesterUtils;
import org.spr.protos.ProtoMessage;
import org.spr.requests.ProtoRequests;
import org.springframework.messaging.rsocket.RSocketRequester;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * This class contains functions to benchmark the throughput of
 * database query endpoints
 */
public class DbReqResTestBench extends BaseTestBench {
    @Benchmark
    @Threads(10)
    public void benchmarkDbGetOneA(ExecutionPlan execPlan) {
        String message = execPlan.presentMessages.get(execPlan.random.nextInt(0, execPlan.presentMessages.size()));
        ProtoMessage response = ProtoRequests.getById(execPlan.rSocketRequester, message);
    }

    @Benchmark
    @Threads(20)
    public void benchmarkDbGetOneB(ExecutionPlan execPlan) {
        String message = execPlan.presentMessages.get(execPlan.random.nextInt(0, execPlan.presentMessages.size()));
        ProtoMessage response = ProtoRequests.getById(execPlan.rSocketRequester, message);
    }

    @Benchmark
    @Threads(50)
    public void benchmarkDbGetOneC(ExecutionPlan execPlan) {
        String message = execPlan.presentMessages.get(execPlan.random.nextInt(0, execPlan.presentMessages.size()));
        ProtoMessage response = ProtoRequests.getById(execPlan.rSocketRequester, message);
    }

    @Benchmark
    @Threads(100)
    public void benchmarkDbGetOneD(ExecutionPlan execPlan) {
        String message = execPlan.presentMessages.get(execPlan.random.nextInt(0, execPlan.presentMessages.size()));
        ProtoMessage response = ProtoRequests.getById(execPlan.rSocketRequester, message);
    }

    @State(Scope.Thread)
    public static class ExecutionPlan {
        public RSocketRequester rSocketRequester;
        public List<String> presentMessages;

        public Random random = new Random(1234);

        @Setup(Level.Iteration)
        public void setUp() {
            rSocketRequester = RequesterUtils.newProtobufRequester(serverHost, serverPort);
            presentMessages = ProtoRequests.getAllMessages(rSocketRequester)
                    .stream().map(ProtoMessage::getId).toList();
        }

        @TearDown(Level.Iteration)
        public void tearDown() {
            rSocketRequester.dispose();
        }
    }

}

