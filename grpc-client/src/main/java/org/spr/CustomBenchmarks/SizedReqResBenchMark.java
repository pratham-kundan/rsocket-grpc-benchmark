package org.spr.CustomBenchmarks;

import org.spr.Requests;

public class SizedReqResBenchMark extends BaseGrpcBenchMark {
    @Override
    public void run() {
        Requests.sizedRequestResponse(sizedMessageServiceStub, 3);
    }
}
