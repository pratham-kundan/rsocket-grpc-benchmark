package org.spr.CustomBenchmarks.Brunch;

import java.lang.reflect.InvocationTargetException;
import java.time.Duration;
import java.time.Instant;

public class BmTaskRunner {
    private BmTask benchmark;
    Duration duration;
    public BmTaskRunner(Class<? extends BmTask> benchmark) throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        var constructor = benchmark.getDeclaredConstructor();
        constructor.setAccessible(true);
        this.benchmark = constructor.newInstance();
        this.duration = Duration.ofMinutes(1);
    }

    public BmTaskRunner(Class<? extends BmTask> benchmark, Duration duration) throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        var constructor = benchmark.getDeclaredConstructor();
        constructor.setAccessible(true);
        this.benchmark = constructor.newInstance();
        this.duration = duration;
    }

    public float execute() {
        var start = Instant.now();
        long runs = 0;
        benchmark.beforeAll();
        while (Instant.now().isBefore(start.plus(duration))) {
            benchmark.beforeEach();
            benchmark.run();
            runs++;
            benchmark.afterEach();
        }
        benchmark.afterAll();
        return ((float)runs/duration.toSeconds());
    }
}
