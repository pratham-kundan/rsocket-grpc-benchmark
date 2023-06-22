package org.spr.components;

import org.spr.protos.SimpleMessage;
import org.spr.requests.ProtoRequests;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.rsocket.RSocketRequester;
import org.springframework.stereotype.Component;

/**
 * Component to help with firing requests for testing
 */
@Component
public class RequestComponent {
    @Autowired
    private final RSocketRequester rSocketRequester;

    public RequestComponent(RSocketRequester rSocketRequester) {
        this.rSocketRequester = rSocketRequester;
    }

    public void fireRequests() {
        // ensure that you set the active profile appropriately (to protobuf or json)
        SimpleMessage response = ProtoRequests.requestResponseProto(rSocketRequester, "Hello");

        System.out.println(response.toString());
    }
}
