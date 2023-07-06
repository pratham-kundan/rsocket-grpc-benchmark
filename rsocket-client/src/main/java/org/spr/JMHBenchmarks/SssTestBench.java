package org.spr.JMHBenchmarks;

import org.openjdk.jmh.annotations.*;
import org.spr.CustomBenchmarks.RequesterUtils;
import org.spr.protos.ProtoMessage;
import org.spr.requests.ProtoRequests;
import org.springframework.messaging.rsocket.RSocketRequester;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Fork(value = 1, warmups = 1)
@BenchmarkMode(Mode.Throughput)
@Warmup(iterations = 1)
@Measurement(iterations = 2, time = 5, timeUnit = TimeUnit.SECONDS)
public class SssTestBench {
    @Benchmark
    @Threads(10)
    public void benchmarkRequestStreamA(ReqResTestBench.ExecutionPlan execPlan) {
        List<ProtoMessage> response = ProtoRequests.requestStream(execPlan.rSocketRequester, "Hello from client");
    }

    @Benchmark
    @Threads(20)
    public void benchmarkRequestStreamB(ReqResTestBench.ExecutionPlan execPlan) {
        List<ProtoMessage> response = ProtoRequests.requestStream(execPlan.rSocketRequester,"Hello from client");
    }

    @Benchmark
    @Threads(50)
    public void benchmarkRequestStreamC(ReqResTestBench.ExecutionPlan execPlan) {
        List<ProtoMessage> response = ProtoRequests.requestStream(execPlan.rSocketRequester, "Hello from client");
    }

    @Benchmark
    @Threads(100)
    public void benchmarkRequestStreamD(ReqResTestBench.ExecutionPlan execPlan) {
        List<ProtoMessage> response = ProtoRequests.requestStream(execPlan.rSocketRequester,"Hello from client");
    }

    @State(Scope.Thread)
    public static class ExecutionPlan {
        public RSocketRequester rSocketRequester;

        @Setup(Level.Iteration)
        public void setUp() {
            rSocketRequester = RequesterUtils.newProtobufRequester("localhost", 8989);
        }

        @TearDown(Level.Iteration)
        public void tearDown() {
            rSocketRequester.dispose();
        }
    }
}
