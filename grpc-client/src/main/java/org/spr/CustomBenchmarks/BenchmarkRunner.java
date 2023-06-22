package org.spr.CustomBenchmarks;

import org.spr.CustomBenchmarks.Brunch.BmCompoundRunner;

import java.time.Duration;

public class BenchmarkRunner {
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
        BmCompoundRunner sizedRequestRunner = new BmCompoundRunner.Builder()
                .addBenchmark(SizedReqResBenchMark.class, Duration.ofMinutes(1), 10, 2)
                .addBenchmark(SizedReqResBenchMark.class, Duration.ofMinutes(1), 20, 2)
                .addBenchmark(SizedReqStreamBenchmark.class, Duration.ofMinutes(1), 10, 2)
                .addBenchmark(SizedReqStreamBenchmark.class, Duration.ofMinutes(1), 20, 2)
                .build();
        sizedRequestRunner.runBenchMark();
    }

    public static void main(String[] args) throws Exception {
        sizedBenchMark();
    }
}
