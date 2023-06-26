package org.spr.CustomBenchmarks;

import org.spr.CustomBench.BmTask;
import org.springframework.messaging.rsocket.RSocketRequester;

public class BaseProtoBenchmark implements BmTask {
    public RSocketRequester rSocketRequester;

    @Override
    public void run() {}

    @Override
    public void beforeAll() {
        rSocketRequester = RequesterUtils.newProtobufRequester("localhost", 8989);
    }

    @Override
    public void afterAll() {
        rSocketRequester.dispose();
    }
}
