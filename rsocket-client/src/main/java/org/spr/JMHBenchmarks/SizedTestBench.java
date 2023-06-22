package org.spr.JMHBenchmarks;

import org.openjdk.jmh.annotations.*;
import org.spr.data.SizedMessage;
import org.spr.requests.JsonRequests;
import org.springframework.http.codec.json.Jackson2JsonDecoder;
import org.springframework.http.codec.json.Jackson2JsonEncoder;
import org.springframework.messaging.rsocket.RSocketRequester;
import org.springframework.messaging.rsocket.RSocketStrategies;
import org.springframework.util.MimeTypeUtils;

import java.util.List;

/**
 * for JSON
 * This class contains functions to benchmark the throughput of
 * server functions returning large objects.
 */
@BenchmarkMode(Mode.Throughput)
@Fork(value = 2)
public class SizedTestBench {

    @Benchmark
    @Fork(value = 1, warmups = 1)
    @BenchmarkMode(Mode.Throughput)
    @Warmup(iterations = 1)
    @Threads(10)
    public void benchmarkSizedRequestResponseT10(ExecutionPlan execPlan) {
        SizedMessage response = JsonRequests.sizedRequestResponse(execPlan.rSocketRequester, 30);
    }

    @Benchmark
    @Fork(value = 1, warmups = 1)
    @BenchmarkMode(Mode.Throughput)
    @Warmup(iterations = 1)
    @Threads(20)
    public void benchmarkSizedRequestResponseT20(ExecutionPlan execPlan) {
        SizedMessage response = JsonRequests.sizedRequestResponse(execPlan.rSocketRequester, 30);
    }

    @Benchmark
    @Fork(value = 1, warmups = 1)
    @BenchmarkMode(Mode.Throughput)
    @Warmup(iterations = 1)
    @Threads(10)
    public void benchmarkSizedRequestStreamT10(ExecutionPlan execPlan) {
        List<SizedMessage> response = JsonRequests.sizedRequestStream(execPlan.rSocketRequester, 100);
    }

    @Benchmark
    @Fork(value = 1, warmups = 1)
    @BenchmarkMode(Mode.Throughput)
    @Warmup(iterations = 1)
    @Threads(20)
    public void benchmarkSizedRequestStreamT20(ExecutionPlan execPlan) {
        List<SizedMessage> response = JsonRequests.sizedRequestStream(execPlan.rSocketRequester, 100);
    }


    @State(Scope.Thread)
    public static class ExecutionPlan {
        public RSocketRequester rSocketRequester = RSocketRequester
                .builder()
                .rsocketStrategies(RSocketStrategies
                        .builder()
                        .encoder(new Jackson2JsonEncoder())
                        .decoder(new Jackson2JsonDecoder())
                        .build()
                )
                .dataMimeType(MimeTypeUtils.APPLICATION_JSON)
                .tcp("localhost", 8989);

        @Setup(Level.Invocation)
        public void setUp() {
        }
    }

}