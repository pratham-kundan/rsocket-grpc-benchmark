package org.spr.JMHBenchmarks;

import org.openjdk.jmh.annotations.*;
import org.spr.CustomBenchmarks.RequesterUtils;
import org.spr.protos.ProtoSizedMessage;
import org.spr.requests.ProtoRequests;
import org.springframework.http.codec.protobuf.ProtobufDecoder;
import org.springframework.http.codec.protobuf.ProtobufEncoder;
import org.springframework.messaging.rsocket.RSocketRequester;
import org.springframework.messaging.rsocket.RSocketStrategies;
import org.springframework.util.MimeType;

import java.util.List;

/**
 * for protobuf
 * This class contains functions to benchmark the throughput of
 * server functions returning large objects.
 */
@BenchmarkMode(Mode.Throughput)
@Fork(value = 2)
public class ProtoSizedTestBench {

    @Benchmark
    @Fork(value = 1, warmups = 1)
    @BenchmarkMode(Mode.Throughput)
    @Warmup(iterations = 1)
    @Threads(10)
    public void benchmarkSizedRequestResponseT10(ExecutionPlan execPlan) {
        ProtoSizedMessage response = ProtoRequests.sizedRequestResponse(execPlan.rSocketRequester, 5);
    }

    @Benchmark
    @Fork(value = 1, warmups = 1)
    @BenchmarkMode(Mode.Throughput)
    @Warmup(iterations = 1)
    @Threads(20)
    public void benchmarkSizedRequestResponseT20(ExecutionPlan execPlan) {
        ProtoSizedMessage response = ProtoRequests.sizedRequestResponse(execPlan.rSocketRequester, 5);
    }

    @Benchmark
    @Fork(value = 1, warmups = 1)
    @BenchmarkMode(Mode.Throughput)
    @Warmup(iterations = 1)
    @Threads(10)
    public void benchmarkSizedRequestStreamT10(ExecutionPlan execPlan) {
        List<ProtoSizedMessage> response = ProtoRequests.sizedRequestStream(execPlan.rSocketRequester, 20);
    }

    @Benchmark
    @Fork(value = 1, warmups = 1)
    @BenchmarkMode(Mode.Throughput)
    @Warmup(iterations = 1)
    @Threads(20)
    public void benchmarkSizedRequestStreamT20(ExecutionPlan execPlan) {
        List<ProtoSizedMessage> response = ProtoRequests.sizedRequestStream(execPlan.rSocketRequester, 20);
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