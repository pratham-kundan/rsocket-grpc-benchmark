package org.spr.JMHBenchmarks;

import org.openjdk.jmh.annotations.*;
import org.spr.CustomBenchmarks.RequesterUtils;
import org.spr.protos.ProtoMessage;
import org.spr.requests.ProtoRequests;
import org.springframework.messaging.rsocket.RSocketRequester;

import java.util.List;

/**
 * This class contains benchmarks for Bidirectional Streaming
 */
public class ChannelTestBench extends BaseTestBench{

    @Benchmark
    @Threads(10)
    public void benchmarkBiStreamA(CssTestBench.ExecutionPlan execPlan) {
        List<ProtoMessage> response = ProtoRequests.biStream(execPlan.rSocketRequester, "Hello from client");
    }


    @Benchmark
    @Threads(20)
    public void benchmarkBiStreamB(CssTestBench.ExecutionPlan execPlan) {
        List<ProtoMessage> response = ProtoRequests.biStream(execPlan.rSocketRequester,"Hello from client");
    }
    @Benchmark
    @Threads(50)
    public void benchmarkBiStreamC(CssTestBench.ExecutionPlan execPlan) {
        List<ProtoMessage> response = ProtoRequests.biStream(execPlan.rSocketRequester, "Hello from client");
    }

    @Benchmark
    @Threads(100)
    public void benchmarkBiStreamD(CssTestBench.ExecutionPlan execPlan) {
        List<ProtoMessage> response = ProtoRequests.biStream(execPlan.rSocketRequester,"Hello from client");
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
