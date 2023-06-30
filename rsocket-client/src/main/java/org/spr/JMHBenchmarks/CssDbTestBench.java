package org.spr.JMHBenchmarks;

import org.openjdk.jmh.annotations.*;
import org.spr.CustomBenchmarks.RequesterUtils;
import org.spr.protos.ProtoMessage;
import org.spr.requests.ProtoRequests;
import org.springframework.messaging.rsocket.RSocketRequester;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * This class contains functions to benchmark the throughput of
 * database query endpoints
 */
@BenchmarkMode(Mode.Throughput)
@Fork(value = 2)
@Measurement(iterations = 3, time = 10, timeUnit = TimeUnit.SECONDS)
public class CssDbTestBench {
    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";

    public static List<String> generateRandomString(int length, int listLength) {
        List<String> messages = new ArrayList<>();

        for (int j = 0; j < listLength; j++) {
            Random random = new Random();
            StringBuilder sb = new StringBuilder(length);

            for (int i = 0; i < length; i++) {
                int randomIndex = random.nextInt(CHARACTERS.length());
                char randomChar = CHARACTERS.charAt(randomIndex);
                sb.append(randomChar);
            }

            messages.add(sb.toString());
        }

        return messages;
    }

    @Benchmark
    @Fork(value = 1, warmups = 1)
    @BenchmarkMode(Mode.Throughput)
    @Warmup(iterations = 1)
    @Threads(10)
    public void benchmarkDbPushRemoveA(ExecutionPlan execPlan) {
        List<ProtoMessage> messageList = ProtoRequests.pushAll(execPlan.rSocketRequester, generateRandomString(100, 100));
        List<String> toBeDeleted = messageList.stream().map(ProtoMessage::getId).toList();
        ProtoMessage response = ProtoRequests.removeAll(execPlan.rSocketRequester, toBeDeleted);
    }

    @Benchmark
    @Fork(value = 1, warmups = 1)
    @BenchmarkMode(Mode.Throughput)
    @Warmup(iterations = 1)
    @Threads(20)
    public void benchmarkDbPushRemoveB(ExecutionPlan execPlan) {
        List<ProtoMessage> messageList = ProtoRequests.pushAll(execPlan.rSocketRequester, generateRandomString(100, 100));
        List<String> toBeDeleted = messageList.stream().map(ProtoMessage::getId).toList();
        ProtoMessage response = ProtoRequests.removeAll(execPlan.rSocketRequester, toBeDeleted);
    }

    @Benchmark
    @Fork(value = 1, warmups = 1)
    @BenchmarkMode(Mode.Throughput)
    @Warmup(iterations = 1)
    @Threads(50)
    public void benchmarkDbPushRemoveC(ExecutionPlan execPlan) {
        List<ProtoMessage> messageList = ProtoRequests.pushAll(execPlan.rSocketRequester, generateRandomString(100, 100));
        List<String> toBeDeleted = messageList.stream().map(ProtoMessage::getId).toList();
        ProtoMessage response = ProtoRequests.removeAll(execPlan.rSocketRequester, toBeDeleted);
    }

    @Benchmark
    @Fork(value = 1, warmups = 1)
    @BenchmarkMode(Mode.Throughput)
    @Warmup(iterations = 1)
    @Threads(100)
    public void benchmarkDbPushRemoveD(ExecutionPlan execPlan) {
        List<ProtoMessage> messageList = ProtoRequests.pushAll(execPlan.rSocketRequester, generateRandomString(100, 100));
        List<String> toBeDeleted = messageList.stream().map(ProtoMessage::getId).toList();
        ProtoMessage response = ProtoRequests.removeAll(execPlan.rSocketRequester, toBeDeleted);
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
