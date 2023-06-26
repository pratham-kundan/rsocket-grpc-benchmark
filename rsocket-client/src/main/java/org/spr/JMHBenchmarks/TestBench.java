package org.spr.JMHBenchmarks;

import org.openjdk.jmh.annotations.*;
import org.spr.CustomBenchmarks.RequesterUtils;
import org.spr.requests.JsonRequests;
import org.springframework.messaging.rsocket.RSocketRequester;

import java.util.List;

/**
 * for Strings
 * This class contains functions to benchmark the throughput of
 * server functions returning echos.
 */
@BenchmarkMode(Mode.Throughput)
@Fork(value = 2)
public class TestBench {

    @Benchmark
    @Fork(value = 1, warmups = 1)
    @BenchmarkMode(Mode.Throughput)
    @Warmup(iterations = 1)
    @Threads(10)
    public void benchmarkRequestResponseT10(ExecutionPlan execPlan) {
        String response = JsonRequests.requestResponseString(execPlan.rSocketRequester, "Hello from client");
    }

    @Benchmark
    @Fork(value = 1, warmups = 1)
    @BenchmarkMode(Mode.Throughput)
    @Warmup(iterations = 1)
    @Threads(20)
    public void benchmarkRequestResponseT20(ExecutionPlan execPlan) {
        String response = JsonRequests.requestResponseString(execPlan.rSocketRequester, "Hello from client");
    }

    @Benchmark
    @Fork(value = 1, warmups = 1)
    @BenchmarkMode(Mode.Throughput)
    @Warmup(iterations = 1)
    @Threads(10)
    public void benchmarkRequestStreamT10(ExecutionPlan execPlan) {
        List<String> response = JsonRequests.requestStreamString(execPlan.rSocketRequester, "Hello from client");
    }

    @Benchmark
    @Fork(value = 1, warmups = 1)
    @BenchmarkMode(Mode.Throughput)
    @Warmup(iterations = 1)
    @Threads(20)
    public void benchmarkRequestStreamT20(ExecutionPlan execPlan) {
        List<String> response = JsonRequests.requestStreamString(execPlan.rSocketRequester, "Hello from client");
    }


    @State(Scope.Thread)
    public static class ExecutionPlan {
        public RSocketRequester rSocketRequester;

        @Setup(Level.Iteration)
        public void setUp() {
            rSocketRequester = RequesterUtils.newJsonRequester("localhost", 8989);
        }

        @TearDown(Level.Iteration)
        public void tearDown() {
            rSocketRequester.dispose();
        }
    }

}

