package org.spr.JMHBenchmarks;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.openjdk.jmh.annotations.*;
import org.spr.requests.Requests;
import org.spr.protos.ProtoSizedMessage;
import org.spr.protos.SizedMessageServiceGrpc;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * for Protobuf
 * This class contains functions to benchmark the throughput of
 * server functions returning large objects.
 */
@BenchmarkMode(Mode.Throughput)
@Fork(value = 1, warmups = 1)
@Warmup(iterations = 1)
@Measurement(iterations = 3, time = 10, timeUnit = TimeUnit.SECONDS)
public class SizedTestBench {

    @Benchmark
    @Threads(10)
    public void benchmarkRequestResponseT10(ExecutionPlan execPlan) {
        ProtoSizedMessage response = Requests.sizedRequestResponse(execPlan.bStub, 30);
    }

    @Benchmark
    @Threads(20)
    public void benchmarkRequestResponseT20(ExecutionPlan execPlan) {
        ProtoSizedMessage response = Requests.sizedRequestResponse(execPlan.bStub, 30);
    }

    @Benchmark
    @Threads(10)
    public void benchmarkRequestStreamT10(ExecutionPlan execPlan) {
        List<ProtoSizedMessage> response = Requests.sizedRequestStream(execPlan.bStub, 100);
    }

    @Benchmark
    @Threads(20)
    public void benchmarkRequestStreamT20(ExecutionPlan execPlan) {
        List<ProtoSizedMessage> response = Requests.sizedRequestStream(execPlan.bStub, 100);
    }

    @State(Scope.Thread)
    public static class ExecutionPlan {
        public ManagedChannel channel;

        public SizedMessageServiceGrpc.SizedMessageServiceBlockingStub bStub;

        @Setup(Level.Iteration)
        public void setUp() {
            channel = ManagedChannelBuilder
                    .forAddress("localhost", 8787)
                    .maxInboundMessageSize(40 * 1024 * 1024)
                    .usePlaintext()
                    .build();

            bStub = SizedMessageServiceGrpc.newBlockingStub(channel);
        }

        @TearDown(Level.Iteration)
        public void tearDown() {
            channel.shutdown();
        }
    }

}
