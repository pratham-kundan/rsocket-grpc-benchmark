package org.spr.JMHBenchmarks;

import org.openjdk.jmh.annotations.*;
import org.spr.CustomBenchmarks.RequesterUtils;
import org.spr.protos.ProtoMessage;
import org.spr.requests.ProtoRequests;
import org.springframework.messaging.rsocket.RSocketRequester;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * This class contains functions to benchmark the throughput of
 * database query endpoints
 */
public class DBTestBench extends BaseTestBench {
    @Benchmark
    @Threads(10)
    public void benchmarkDbGetAllA(ExecutionPlan execPlan) {
        List<ProtoMessage> messageDtoList = ProtoRequests.getAllMessages(execPlan.rSocketRequester);
    }

    @Benchmark
    @Threads(20)
    public void benchmarkDbGetAllB(ExecutionPlan execPlan) {
        List<ProtoMessage> messageDtoList = ProtoRequests.getAllMessages(execPlan.rSocketRequester);
    }

    @Benchmark
    @Threads(50)
    public void benchmarkDbGetAllC(ExecutionPlan execPlan) {
        List<ProtoMessage> messageDtoList = ProtoRequests.getAllMessages(execPlan.rSocketRequester);
    }

    @Benchmark
    @Threads(100)
    public void benchmarkDbGetAllD(ExecutionPlan execPlan) {
        List<ProtoMessage> messageDtoList = ProtoRequests.getAllMessages(execPlan.rSocketRequester);
    }

    @State(Scope.Thread)
    public static class ExecutionPlan {
        public RSocketRequester rSocketRequester;

        @Setup(Level.Iteration)
        public void setUp() {
            rSocketRequester = RequesterUtils.newProtobufRequester(serverHost, serverPort);
        }

        @TearDown(Level.Iteration)
        public void tearDown() {
            rSocketRequester.dispose();
        }
    }

}

