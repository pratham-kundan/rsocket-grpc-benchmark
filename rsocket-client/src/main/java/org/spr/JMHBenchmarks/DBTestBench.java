package org.spr.JMHBenchmarks;

import org.openjdk.jmh.annotations.*;
import org.spr.CustomBenchmarks.RequesterUtils;
import org.spr.data.MessageDto;
import org.spr.protos.ProtoMessage;
import org.spr.requests.JsonRequests;
import org.spr.requests.ProtoRequests;
import org.springframework.http.codec.json.Jackson2JsonDecoder;
import org.springframework.http.codec.json.Jackson2JsonEncoder;
import org.springframework.messaging.rsocket.RSocketRequester;
import org.springframework.messaging.rsocket.RSocketStrategies;
import org.springframework.util.MimeTypeUtils;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * This class contains functions to benchmark the throughput of
 * database query endpoints
 */
@BenchmarkMode(Mode.Throughput)
@Fork(value = 2)
@Measurement(iterations = 3, time = 10, timeUnit = TimeUnit.SECONDS)
public class DBTestBench {
    @Benchmark
    @Fork(value = 1, warmups = 1)
    @BenchmarkMode(Mode.Throughput)
    @Warmup(iterations = 1)
    @Threads(10)
    public void benchmarkDbGetAllA(ExecutionPlan execPlan) {
        List<ProtoMessage> messageDtoList = ProtoRequests.getAllMessages(execPlan.rSocketRequester);
    }

    @Benchmark
    @Fork(value = 1, warmups = 1)
    @BenchmarkMode(Mode.Throughput)
    @Warmup(iterations = 1)
    @Threads(20)
    public void benchmarkDbGetAllB(ExecutionPlan execPlan) {
        List<ProtoMessage> messageDtoList = ProtoRequests.getAllMessages(execPlan.rSocketRequester);
    }

    @Benchmark
    @Fork(value = 1, warmups = 1)
    @BenchmarkMode(Mode.Throughput)
    @Warmup(iterations = 1)
    @Threads(50)
    public void benchmarkDbGetAllC(ExecutionPlan execPlan) {
        List<ProtoMessage> messageDtoList = ProtoRequests.getAllMessages(execPlan.rSocketRequester);
    }

    @Benchmark
    @Fork(value = 1, warmups = 1)
    @BenchmarkMode(Mode.Throughput)
    @Warmup(iterations = 1)
    @Threads(100)
    public void benchmarkDbGetAllD(ExecutionPlan execPlan) {
        List<ProtoMessage> messageDtoList = ProtoRequests.getAllMessages(execPlan.rSocketRequester);
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

