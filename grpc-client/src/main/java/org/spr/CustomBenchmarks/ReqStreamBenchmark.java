package org.spr.CustomBenchmarks;

import org.spr.requests.Requests;

public class ReqStreamBenchmark extends BaseGrpcBenchMark {
    @Override
    public void run() {
        Requests.requestStream(messageServiceStub, "Hello from Client");
    }
}
