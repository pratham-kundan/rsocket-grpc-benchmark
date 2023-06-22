package org.spr.CustomBenchmarks.Brunch;

public interface BmTask {
    void run();

    default void beforeAll() {
    }

    default void beforeEach() {
    }

    default void afterAll() {
    }

    default void afterEach() {
    }
}
