package org.spr.CustomBenchmarks;

import org.spr.Requests;

public class ReqResBenchmark extends BaseGrpcBenchMark {
    @Override
    public void run() {
        Requests.requestResponse(messageServiceStub, "Hello from Client");
    }
}
