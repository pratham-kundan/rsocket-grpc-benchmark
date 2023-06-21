package org.spr;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.openjdk.jmh.annotations.*;
import org.spr.protos.*;

import java.util.List;

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
        SimpleSizedMessage response = RequestComponent.sizedRequestResponse(execPlan.bStub, 3);
    }

//    @Benchmark
//    @Fork(value = 1, warmups = 1)
//    @BenchmarkMode(Mode.Throughput)
//    @Warmup(iterations = 1)
//    @Threads(20)
//    public void benchmarkRequestResponseT20(ExecutionPlan execPlan) {
//        SimpleSizedMessage response = RequestComponent.sizedRequestResponse(execPlan.bStub, 3);
//    }

//    @Benchmark
//    @Fork(value = 1, warmups = 1)
//    @BenchmarkMode(Mode.Throughput)
//    @Warmup(iterations = 1)
//    @Threads(10)
//    public void benchmarkRequestStreamT10(ExecutionPlan execPlan) {
//        List<SimpleSizedMessage> response = RequestComponent.sizedRequestStream(execPlan.bStub, 10);
//    }

//    @Benchmark
//    @Fork(value = 1, warmups = 1)
//    @BenchmarkMode(Mode.Throughput)
//    @Warmup(iterations = 1)
//    @Threads(20)
//    public void benchmarkRequestStreamT20(ExecutionPlan execPlan) {
//        List<SimpleSizedMessage> response = RequestComponent.sizedRequestStream(execPlan.bStub, 10);
//    }

    @State(Scope.Thread)
    public static class ExecutionPlan {
        public ManagedChannel channel = ManagedChannelBuilder
                .forAddress("localhost", 8787)
                .usePlaintext()
                .build();

        public SizedMessageServiceGrpc.SizedMessageServiceBlockingStub bStub;

        @Setup(Level.Invocation)
        public void setUp() {
            bStub = SizedMessageServiceGrpc.newBlockingStub(channel);
        }
    }

}
