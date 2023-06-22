package org.spr.JMHBenchmarks;

import org.openjdk.jmh.annotations.*;
import org.spr.data.MessageDto;
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
 * server functions returning echos.
 */
@BenchmarkMode(Mode.Throughput)
@Fork(value = 2)
public class DtoTestBench {

    @Benchmark
    @Fork(value = 1, warmups = 1)
    @BenchmarkMode(Mode.Throughput)
    @Warmup(iterations = 1)
    @Threads(10)
    public void benchmarkRequestResponseDtoT10(ExecutionPlan execPlan) {
        MessageDto response = JsonRequests.requestResponse(execPlan.rSocketRequester,  "Hello from client");
    }

    @Benchmark
    @Fork(value = 1, warmups = 1)
    @BenchmarkMode(Mode.Throughput)
    @Warmup(iterations = 1)
    @Threads(20)
    public void benchmarkRequestResponseDtoT20(ExecutionPlan execPlan) {
        MessageDto response = JsonRequests.requestResponse(execPlan.rSocketRequester, "Hello from client");
    }

    @Benchmark
    @Fork(value = 1, warmups = 1)
    @BenchmarkMode(Mode.Throughput)
    @Warmup(iterations = 1)
    @Threads(10)
    public void benchmarkRequestStreamDtoT10(ExecutionPlan execPlan) {
        List<MessageDto> response = JsonRequests.requestStream(execPlan.rSocketRequester, "Hello from client");
    }

    @Benchmark
    @Fork(value = 1, warmups = 1)
    @BenchmarkMode(Mode.Throughput)
    @Warmup(iterations = 1)
    @Threads(20)
    public void benchmarkRequestStreamDtoT20(ExecutionPlan execPlan) {
        List<MessageDto> response = JsonRequests.requestStream(execPlan.rSocketRequester,"Hello from client");
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

