package org.spr.JMHBenchmarks;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.openjdk.jmh.annotations.*;
import org.spr.protos.MessageServiceGrpc;
import org.spr.protos.ProtoMessage;
import org.spr.requests.Requests;

import java.util.List;
import java.util.concurrent.TimeUnit;


@Fork(value = 1, warmups = 1)
@BenchmarkMode(Mode.Throughput)
@Warmup(iterations = 1)
@Measurement(iterations = 3, time = 10, timeUnit = TimeUnit.SECONDS)
public class SssTestBench {
    @Benchmark
    @Threads(10)
    public void benchmarkRequestStreamA(ReqResTestBench.ExecutionPlan execPlan) {
        List<ProtoMessage> response = Requests.requestStream(execPlan.bStub, "Hello from client");
    }

    @Benchmark
    @Threads(20)
    public void benchmarkRequestStreamB(ReqResTestBench.ExecutionPlan execPlan) {
        List<ProtoMessage> response = Requests.requestStream(execPlan.bStub, "Hello from client");
    }

    @Benchmark
    @Threads(50)
    public void benchmarkRequestStreamC(ReqResTestBench.ExecutionPlan execPlan) {
        List<ProtoMessage> response = Requests.requestStream(execPlan.bStub, "Hello from client");
    }

    @Benchmark
    @Threads(100)
    public void benchmarkRequestStreamD(ReqResTestBench.ExecutionPlan execPlan) {
        List<ProtoMessage> response = Requests.requestStream(execPlan.bStub, "Hello from client");
    }

    @State(Scope.Thread)
    public static class ExecutionPlan {
        public ManagedChannel channel;

        public MessageServiceGrpc.MessageServiceBlockingStub bStub;

        @Setup(Level.Invocation)
        public void setUp() {
            channel = ManagedChannelBuilder
                    .forAddress("localhost", 8787)
                    .usePlaintext()
                    .build();

            bStub = MessageServiceGrpc.newBlockingStub(channel);
        }

        @TearDown(Level.Invocation)
        public void tearDown() {
            channel.shutdown();
        }
    }
}
