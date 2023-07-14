package org.spr.JMHBenchmarks;

import org.openjdk.jmh.results.format.ResultFormatType;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.util.HashMap;

/**
 * Runner for the test benchmarks for RSockets
 */
public class RsJmhRunner {
    public static void main(String[] args) throws Exception {
        HashMap<String, String> name_map = new HashMap<>();
        name_map.put(ReqResTestBench.class.getName(), "request_response");
        name_map.put(ReqStreamTestBench.class.getName(), "request_stream");
        name_map.put(StreamResTestBench.class.getName(), "stream_response");
        name_map.put(ChannelTestBench.class.getName(), "channel");
        name_map.put(DbReqResTestBench.class.getName(), "db_request_response");
        name_map.put(DbReqStreamTestBench.class.getName(), "db_request_stream");
        name_map.put(DbBiStreamTestBench.class.getName(), "db_bi_stream");
        name_map.put(ProtoSizedTestBench.class.getName(), "large_sized");

        // replace with benchmark to be run
        String chosen = ChannelTestBench.class.getName();

        Options options = new OptionsBuilder()
                .include(chosen)
                .resultFormat(ResultFormatType.CSV)
                .result("./rs_" + name_map.get(chosen) + "_tpt.csv")    // Path of throughput csv
                .build();

        new Runner(options).run();
    }
}
