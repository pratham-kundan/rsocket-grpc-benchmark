package org.spr.CustomBenchmarks;

import org.spr.CustomBench.BmCompoundRunner;
import org.spr.requests.ProtoRequests;

import java.time.Duration;


public class RsBmRunner {
    public static void simpleBenchMark() throws Exception {
        BmCompoundRunner simpleRequestResponseRunner = new BmCompoundRunner.Builder()
                .addBenchmark(ReqResBenchmark.class, Duration.ofMinutes(1), 10, 2)
                .addBenchmark(ReqResBenchmark.class, Duration.ofMinutes(1), 20, 2)
                .addBenchmark(ReqStreamBenchmark.class, Duration.ofMinutes(1), 10, 2)
                .addBenchmark(ReqStreamBenchmark.class, Duration.ofMinutes(1), 20, 2)
                .build();
        simpleRequestResponseRunner.runBenchMark();
    }

    public static void dbBenchMark() throws Exception {
        BmCompoundRunner dbQueryRunner = new BmCompoundRunner.Builder()
                .addBenchmark(DbReqStreamBenchmark.class, Duration.ofMinutes(1), 10, 2)
                .addBenchmark(DbReqStreamBenchmark.class, Duration.ofMinutes(1), 20, 2)
                .addBenchmark(DbReqStreamBenchmark.class, Duration.ofMinutes(1), 50, 2)
                .addBenchmark(DbReqStreamBenchmark.class, Duration.ofMinutes(1), 100, 2)
                .build();

        dbQueryRunner.runBenchMark();
    }

    public static void sizedBenchMark() throws Exception {
        BmCompoundRunner sizedQueryRunner = new BmCompoundRunner.Builder()
                .addBenchmark(SizedReqResBenchmark.class, Duration.ofMinutes(1), 10, 2)
                .addBenchmark(SizedReqResBenchmark.class, Duration.ofMinutes(1), 20, 2)
                .addBenchmark(SizedReqStreamBenchmark.class, Duration.ofMinutes(1), 10, 2)
                .addBenchmark(SizedReqStreamBenchmark.class, Duration.ofMinutes(1), 20, 2)
                .build();

        sizedQueryRunner.runBenchMark();
    }

    public static void main(String[] args) throws Exception {
        sizedBenchMark();
    }

    public static class ReqResBenchmark extends BaseProtoBenchmark {
        @Override
        public void run() {
            ProtoRequests.requestResponse(rSocketRequester, "Hello from Client");
        }
    }

    public static class SizedReqResBenchmark extends BaseProtoBenchmark {
        @Override
        public void run() {
            ProtoRequests.sizedRequestResponse(rSocketRequester, 3);
        }
    }

    public static class ReqStreamBenchmark extends BaseProtoBenchmark {
        @Override
        public void run() {
            ProtoRequests.requestStream(rSocketRequester, "Hello from Client");
        }
    }

    public static class SizedReqStreamBenchmark extends BaseProtoBenchmark {
        @Override
        public void run() {
            ProtoRequests.sizedRequestStream(rSocketRequester, 10);
        }
    }

    public static class DbReqStreamBenchmark extends BaseProtoBenchmark {
        @Override
        public void run() {
            ProtoRequests.getAllMessages(rSocketRequester);
        }
    }
}
