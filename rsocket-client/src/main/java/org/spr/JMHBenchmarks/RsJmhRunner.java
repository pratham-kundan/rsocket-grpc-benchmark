package org.spr.JMHBenchmarks;

import org.openjdk.jmh.results.format.ResultFormatType;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

/**
 * Runner for the test benchmarks
 */
public class RsJmhRunner {
    public static void main(String[] args) throws Exception {
        Options options = new OptionsBuilder()
                .include(ProtoSizedTestBench.class.getName())
                .resultFormat(ResultFormatType.CSV)
                .result("./Results/container-csv/rs_med_sized_tpt.csv")
                .build();

        new Runner(options).run();
    }
}
