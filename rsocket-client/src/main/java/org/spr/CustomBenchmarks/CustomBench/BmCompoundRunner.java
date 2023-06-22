package org.spr.CustomBenchmarks.CustomBench;

import java.time.Duration;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

public class BmCompoundRunner {
    ArrayList<BmTaskDescriptor> benchmarkList;
    ArrayList<BmResult> results;

    private BmCompoundRunner() {
        benchmarkList = new ArrayList<>();
        results = new ArrayList<>();
    }

    public void runBenchMark() throws InterruptedException {
        for (BmTaskDescriptor task : benchmarkList) {
            System.out.println("Executing: " + task.getTask().getName() + " on: " + task.getThreads() + " Threads");
            for (int r = 0; r < task.getRepeat(); r++) {
                ExecutorService executorService = Executors.newFixedThreadPool(task.getThreads());
                AtomicReference<Float> totalThroughput = new AtomicReference<>((float) 0);

                System.out.println("Iteration: " + (r+1) + " of " + task.getRepeat());

                for (int i = 0; i < task.getThreads(); i++) {
                    executorService.submit(() -> {
                        try {
                            BmTaskRunner runner = task.getTaskRunner();
                            float taskThroughput = runner.execute();

                            totalThroughput.updateAndGet(v -> v + taskThroughput);
                        } catch (Exception e) {
                            System.out.println(e.getMessage());
                            throw new RuntimeException(e);
                        }
                    });
                }

                executorService.shutdown();
                boolean terminated = executorService.awaitTermination(10, TimeUnit.MINUTES);
                if (terminated) {
                    results.add(new BmResult(task.getTask().getName(), "COMPLETE", task.getRuntimeDuration().toString(), task.getThreads(), totalThroughput.get()));
                    System.out.println(results.get(results.size() - 1).toString());
                } else {
                    results.add(new BmResult(task.getTask().getName(), "TIMED_OUT", task.getRuntimeDuration().toString(), task.getThreads(), -1));
                }
            }
        }
    }

    public ArrayList<BmResult> getResults() {
        return this.results;
    }

    public static class Builder {
        private final BmCompoundRunner bmCompundRunner;

        public Builder() {
            this.bmCompundRunner = new BmCompoundRunner();
        }

        public Builder addBenchmark(Class<? extends BmTask> benchmark, Duration duration, int threads, int repeat) {
            bmCompundRunner.benchmarkList.add(new BmTaskDescriptor(benchmark, threads, duration, repeat));
            return this;
        }

        public Builder addBenchmark(Class<? extends BmTask> benchmark, Duration duration, int threads) {
            bmCompundRunner.benchmarkList.add(new BmTaskDescriptor(benchmark, threads, duration, 1));
            return this;
        }

        public Builder addBenchmark(Class<? extends BmTask> benchmark, int threads) {
            bmCompundRunner.benchmarkList.add(new BmTaskDescriptor(benchmark, threads, Duration.ofMinutes(1), 1));
            return this;
        }

        public Builder addBenchmark(Class<? extends BmTask> benchmark) {
            bmCompundRunner.benchmarkList.add(new BmTaskDescriptor(benchmark, 1, Duration.ofMinutes(1), 1));
            return this;
        }

        public BmCompoundRunner build() {
            return bmCompundRunner;
        }

    }
}
