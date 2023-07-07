package org.spr.JMHBenchmarks;

import org.openjdk.jmh.annotations.*;
import org.spr.CustomBenchmarks.RequesterUtils;
import org.spr.protos.ProtoMessage;
import org.spr.requests.ProtoRequests;
import org.springframework.messaging.rsocket.RSocketRequester;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * for JSON
 * This class contains functions to benchmark the throughput of
 * server functions returning echos.
 */

public class ReqResTestBench extends BaseTestBench {

    @Benchmark
    @Threads(10)
    public void benchmarkRequestResponseA(ExecutionPlan execPlan) {
        ProtoMessage response = ProtoRequests.requestResponse(execPlan.rSocketRequester,  "Hello from client");
    }

    @Benchmark
    @Threads(20)
    public void benchmarkRequestResponseB(ExecutionPlan execPlan) {
        ProtoMessage response = ProtoRequests.requestResponse(execPlan.rSocketRequester, "Hello from client");
    }

    @Benchmark
    @Threads(50)
    public void benchmarkRequestResponseC(ExecutionPlan execPlan) {
        ProtoMessage response = ProtoRequests.requestResponse(execPlan.rSocketRequester,  "Hello from client");
    }

    @Benchmark
    @Threads(100)
    public void benchmarkRequestResponseD(ExecutionPlan execPlan) {
        ProtoMessage response = ProtoRequests.requestResponse(execPlan.rSocketRequester, "Hello from client");
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

