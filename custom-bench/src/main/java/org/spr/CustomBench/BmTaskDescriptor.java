package org.spr.CustomBench;

import java.lang.reflect.InvocationTargetException;
import java.time.Duration;

public class BmTaskDescriptor {
    Class<? extends BmTask> task;
    int threads;
    int repeat;
    Duration runtimeDuration;

    public BmTaskDescriptor(Class<? extends BmTask> task, int threads, Duration runtimeDuration, int repeat) {
        this.task = task;
        this.threads = threads;
        this.runtimeDuration = runtimeDuration;
        this.repeat = repeat;
    }

    public BmTaskRunner getTaskRunner() throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        return new BmTaskRunner(task, runtimeDuration);
    }

    public Class<? extends BmTask> getTask() {
        return task;
    }

    public int getThreads() {
        return threads;
    }

    public Duration getRuntimeDuration() {
        return runtimeDuration;
    }

    public int getRepeat() {
        return repeat;
    }
}
