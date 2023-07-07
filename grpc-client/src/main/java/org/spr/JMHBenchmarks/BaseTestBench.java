package org.spr.JMHBenchmarks;

import org.openjdk.jmh.annotations.*;
import org.spr.utils.PropertiesLoader;

import java.util.concurrent.TimeUnit;

@BenchmarkMode(Mode.Throughput)
@Fork(value = 0, warmups = 1)
@Warmup(iterations = 1)
@Measurement(iterations = 3, time = 10, timeUnit = TimeUnit.SECONDS)
public class BaseTestBench {
    public static String serverHost = PropertiesLoader.getProperty("server_host");
    public static int serverPort = Integer.parseInt(PropertiesLoader.getProperty("server_port"));
}
