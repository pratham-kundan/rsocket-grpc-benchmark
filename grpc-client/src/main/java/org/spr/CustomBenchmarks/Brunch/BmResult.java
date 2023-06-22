package org.spr.CustomBenchmarks.Brunch;

public class BmResult {
    String status;
    String name;
    String time;
    int threads;
    float throughput;

    public BmResult(String name, String status, String time, int threads, float throughput) {
        this.name = name;
        this.status = status;
        this.time = time;
        this.threads = threads;
        this.throughput = throughput;
    }

    @Override
    public String toString() {
        return  ("Name: " + name + "\tStatus: " + status + "\tTime: " + time + "\nthreads: " + threads + "\tThroughput: " + throughput);
    }
}
