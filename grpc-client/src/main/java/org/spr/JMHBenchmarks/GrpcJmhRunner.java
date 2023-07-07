package org.spr.JMHBenchmarks;

import org.openjdk.jmh.results.format.ResultFormatType;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;
import org.spr.CustomBenchmarks.DbReqStreamBenchmark;

import java.util.List;

public class GrpcJmhRunner {
    public static void main(String[] args) throws Exception {
        Options options = new OptionsBuilder()
                .include(SssTestBench.class.getName())
                .resultFormat(ResultFormatType.CSV)
                .result("grpc-results.csv")
                .build();

        new Runner(options).run();
    }
}
