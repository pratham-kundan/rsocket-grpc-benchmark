package org.spr.JmhBenchmarks;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.openjdk.jmh.annotations.*;
import org.spr.Requests;
import org.spr.protos.SimpleSizedMessage;
import org.spr.protos.SizedMessageServiceGrpc;

import java.util.List;

/**
 * for Protobuf
 * This class contains functions to benchmark the throughput of
 * server functions returning large objects.
 */
@BenchmarkMode(Mode.Throughput)
@Fork(value = 2)
@Threads(100)
public class SizedTestBench {

    @Benchmark
    @Fork(value = 1, warmups = 1)
    @BenchmarkMode(Mode.Throughput)
    @Warmup(iterations = 1)
    @Threads(10)
    public void benchmarkRequestResponseT10(ExecutionPlan execPlan) {
        SimpleSizedMessage response = Requests.sizedRequestResponse(execPlan.bStub, 30);
    }

    @Benchmark
    @Fork(value = 1, warmups = 1)
    @BenchmarkMode(Mode.Throughput)
    @Warmup(iterations = 1)
    @Threads(20)
    public void benchmarkRequestResponseT20(ExecutionPlan execPlan) {
        SimpleSizedMessage response = Requests.sizedRequestResponse(execPlan.bStub, 30);
    }

    @Benchmark
    @Fork(value = 1, warmups = 1)
    @BenchmarkMode(Mode.Throughput)
    @Warmup(iterations = 1)
    @Threads(10)
    public void benchmarkRequestStreamT10(ExecutionPlan execPlan) {
        List<SimpleSizedMessage> response = Requests.sizedRequestStream(execPlan.bStub, 100);
    }

    @Benchmark
    @Fork(value = 1, warmups = 1)
    @BenchmarkMode(Mode.Throughput)
    @Warmup(iterations = 1)
    @Threads(20)
    public void benchmarkRequestStreamT20(ExecutionPlan execPlan) {
        List<SimpleSizedMessage> response = Requests.sizedRequestStream(execPlan.bStub, 100);
    }

    @State(Scope.Thread)
    public static class ExecutionPlan {
        public ManagedChannel channel = ManagedChannelBuilder
                .forAddress("localhost", 8787)
                .maxInboundMessageSize(40 * 1024 * 1024)
                .usePlaintext()
                .build();

        public SizedMessageServiceGrpc.SizedMessageServiceBlockingStub bStub = SizedMessageServiceGrpc.newBlockingStub(channel);

        @Setup(Level.Invocation)
        public void setUp() {
//            bStub = SizedMessageServiceGrpc.newBlockingStub(channel);
        }
    }

}
