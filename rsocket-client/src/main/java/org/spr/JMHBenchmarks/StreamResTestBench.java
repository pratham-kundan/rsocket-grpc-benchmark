package org.spr.JMHBenchmarks;

import org.openjdk.jmh.annotations.*;
import org.spr.CustomBenchmarks.RequesterUtils;
import org.spr.protos.ProtoMessage;
import org.spr.requests.ProtoRequests;
import org.springframework.messaging.rsocket.RSocketRequester;

/**
 * This class contains benchmarks for Client Side Streaming.
 * <p>
 * Sends a stream of messages to the server and awaits a response at the end of the stream
 */
public class StreamResTestBench extends BaseTestBench {

    @Benchmark
    @Threads(10)
    public void benchmarkStreamResponseA(ExecutionPlan execPlan) {
        ProtoMessage response = ProtoRequests.streamResponse(execPlan.rSocketRequester, "Hello from client");
    }

    @Benchmark
    @Threads(20)
    public void benchmarkStreamResponseB(ExecutionPlan execPlan) {
        ProtoMessage response = ProtoRequests.streamResponse(execPlan.rSocketRequester, "Hello from client");
    }

    @Benchmark
    @Threads(50)
    public void benchmarkStreamResponseC(ExecutionPlan execPlan) {
        ProtoMessage response = ProtoRequests.streamResponse(execPlan.rSocketRequester, "Hello from client");
    }

    @Benchmark
    @Threads(100)
    public void benchmarkStreamResponseD(ExecutionPlan execPlan) {
        ProtoMessage response = ProtoRequests.streamResponse(execPlan.rSocketRequester, "Hello from client");
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

