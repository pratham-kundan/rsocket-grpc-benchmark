package org.spr;

import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

public class BenchMarkRunner {
    public static void main(String[] args) throws Exception {
        Options options = new OptionsBuilder()
                .include(TestBench.class.getSimpleName())
                .build();

        new Runner(options).run();
    }
}
