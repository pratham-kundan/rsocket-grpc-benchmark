package org.spr.JMHBenchmarks;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.openjdk.jmh.annotations.*;
import org.spr.protos.ProtoMessage;
import org.spr.requests.Requests;
import org.spr.protos.MessageServiceGrpc;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * for Protobuf
 * This class contains functions to benchmark the throughput of
 * server functions returning echos.
 */
@BenchmarkMode(Mode.Throughput)
@Fork(value = 1, warmups = 1)
@Warmup(iterations = 1)
@Measurement(iterations = 3, time = 10, timeUnit = TimeUnit.SECONDS)
public class ChannelTestBench {

    @Benchmark
    @Threads(10)
    public void benchmarkChannelA(ExecutionPlan execPlan) throws InterruptedException {
        List<ProtoMessage> response = Requests.biStream(execPlan.bStub, "Hello from client");
    }
    @Benchmark
    @Threads(20)
    public void benchmarkChannelB(ExecutionPlan execPlan) throws InterruptedException {
        List<ProtoMessage> response = Requests.biStream(execPlan.bStub, "Hello from client");
    }

    @Benchmark
    @Threads(50)
    public void benchmarkChannelC(ExecutionPlan execPlan) throws InterruptedException {
        List<ProtoMessage> response = Requests.biStream(execPlan.bStub, "Hello from client");
    }


    @Benchmark
    @Threads(100)
    public void benchmarkChannelD(ExecutionPlan execPlan) throws InterruptedException {
        List<ProtoMessage> response = Requests.biStream(execPlan.bStub, "Hello from client");
    }



    @State(Scope.Thread)
    public static class ExecutionPlan {
        public ManagedChannel channel;

        public MessageServiceGrpc.MessageServiceStub bStub;

        @Setup(Level.Iteration)
        public void setUp() {
            channel = ManagedChannelBuilder
                    .forAddress("localhost", 8787)
                    .usePlaintext()
                    .build();

            bStub = MessageServiceGrpc.newStub(channel);
        }

        @TearDown(Level.Iteration)
        public void tearDown() {
            channel.shutdown();
        }
    }

}