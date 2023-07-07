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
import java.util.concurrent.TimeUnit;

/**
 * for protobuf
 * This class contains functions to benchmark the throughput of
 * server functions returning large objects.
 */

public class ProtoSizedTestBench extends BaseTestBench {

    @Benchmark
    @Threads(10)
    public void benchmarkSizedRequestResponseT10(ExecutionPlan execPlan) {
        ProtoSizedMessage response = ProtoRequests.sizedRequestResponse(execPlan.rSocketRequester, 30);
    }

    @Benchmark
    @Threads(20)
    public void benchmarkSizedRequestResponseT20(ExecutionPlan execPlan) {
        ProtoSizedMessage response = ProtoRequests.sizedRequestResponse(execPlan.rSocketRequester, 30);
    }

    @Benchmark
    @Threads(10)
    public void benchmarkSizedRequestStreamT10(ExecutionPlan execPlan) {
        List<ProtoSizedMessage> response = ProtoRequests.sizedRequestStream(execPlan.rSocketRequester, 100);
    }

    @Benchmark
    @Threads(20)
    public void benchmarkSizedRequestStreamT20(ExecutionPlan execPlan) {
        List<ProtoSizedMessage> response = ProtoRequests.sizedRequestStream(execPlan.rSocketRequester, 100);
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