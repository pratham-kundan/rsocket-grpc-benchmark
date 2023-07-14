package org.spr.JMHBenchmarks;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.openjdk.jmh.annotations.*;
import org.spr.protos.ProtoSizedMessage;
import org.spr.protos.SizedMessageServiceGrpc;
import org.spr.requests.Requests;

import java.util.List;

/**
 * This class contains functions to benchmark the throughput of
 * server functions returning large objects.
 */
public class SizedTestBench extends BaseTestBench {

    @Benchmark
    @Threads(10)
    public void benchmarkRequestResponseT10(ExecutionPlan execPlan) {
        ProtoSizedMessage response = Requests.sizedRequestResponse(execPlan.bStub, 5);
    }

    @Benchmark
    @Threads(20)
    public void benchmarkRequestResponseT20(ExecutionPlan execPlan) {
        ProtoSizedMessage response = Requests.sizedRequestResponse(execPlan.bStub, 5);
    }

    @Benchmark
    @Threads(10)
    public void benchmarkRequestStreamT10(ExecutionPlan execPlan) {
        List<ProtoSizedMessage> response = Requests.sizedRequestStream(execPlan.bStub, 5);
    }

    @Benchmark
    @Threads(20)
    public void benchmarkRequestStreamT20(ExecutionPlan execPlan) {
        List<ProtoSizedMessage> response = Requests.sizedRequestStream(execPlan.bStub, 5);
    }

    @State(Scope.Benchmark)
    public static class ExecutionPlan {
        public ManagedChannel channel;

        public SizedMessageServiceGrpc.SizedMessageServiceBlockingStub bStub;

        @Setup(Level.Iteration)
        public void setUp() {
            channel = ManagedChannelBuilder
                    .forAddress(serverHost, serverPort)
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
//
//    Benchmark                                                  Mode  Cnt    Score     Error  Units
//    JMHBenchmarks.SizedTestBench.benchmarkRequestResponseT10  thrpt    3  220.898 ± 443.723  ops/s
//        JMHBenchmarks.SizedTestBench.benchmarkRequestResponseT20  thrpt    3  248.794 ± 147.478  ops/s
//        JMHBenchmarks.SizedTestBench.benchmarkRequestStreamT10    thrpt    3  280.372 ± 216.558  ops/s
//        JMHBenchmarks.SizedTestBench.benchmarkRequestStreamT20    thrpt    3  272.849 ± 226.744  ops/s