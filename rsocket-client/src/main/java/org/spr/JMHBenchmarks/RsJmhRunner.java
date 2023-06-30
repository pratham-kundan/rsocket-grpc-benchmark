package org.spr.JMHBenchmarks;

import org.openjdk.jmh.results.format.ResultFormatType;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

public class RsJmhRunner {
    public static void main(String[] args) throws Exception {
        Options options = new OptionsBuilder()
                .include(CssDbTestBench.class.getName())
                .resultFormat(ResultFormatType.CSV)
                .result("rsocket-results.csv")
                .build();

        new Runner(options).run();
    }
}
