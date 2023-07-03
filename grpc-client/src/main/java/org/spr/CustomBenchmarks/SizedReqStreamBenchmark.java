package org.spr.CustomBenchmarks;

import org.spr.requests.Requests;

public class SizedReqStreamBenchmark extends BaseGrpcBenchMark{
    @Override
    public void run() {
        Requests.sizedRequestStream(sizedMessageServiceStub, 10);
    }
}
