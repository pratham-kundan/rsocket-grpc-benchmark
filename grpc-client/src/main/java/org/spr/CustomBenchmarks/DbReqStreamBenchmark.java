package org.spr.CustomBenchmarks;

import org.spr.requests.Requests;

public class DbReqStreamBenchmark extends BaseGrpcBenchMark {
    @Override
    public void run() {
        Requests.getAllMessages(dbServiceStub);
    }
}
