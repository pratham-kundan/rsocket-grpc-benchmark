package org.spr.requests;

import org.spr.protos.ProtoMessage;
import org.spr.protos.ProtoSizedMessage;
import org.spr.protos.SizedMessageRequest;
import org.springframework.messaging.rsocket.RSocketRequester;

import java.util.List;

/**
 * Class to make requests to server and return protobuf objects
 */
public class ProtoRequests {
    public static ProtoMessage requestResponse(RSocketRequester rSocketRequester, String requestText) {
        return rSocketRequester.route("request-response-proto")
                .data(ProtoMessage.newBuilder().setBody(requestText).build())
                .retrieveMono(ProtoMessage.class)
                .block();
    }

    public static List<ProtoMessage> requestStream(RSocketRequester rSocketRequester, String requestText) {
        return rSocketRequester.route("request-stream-proto")
                .data(ProtoMessage.newBuilder().setBody(requestText).build())
                .retrieveFlux(ProtoMessage.class)
                .collectList()
                .block();
    }

    public static List<ProtoMessage> getAllMessages(RSocketRequester rSocketRequester) {
        return rSocketRequester
                .route("get-all-proto")
                .retrieveFlux(ProtoMessage.class)
                .collectList()
                .block();
    }

    public static ProtoSizedMessage sizedRequestResponse(RSocketRequester rSocketRequester, int sizeMb) {
        return rSocketRequester.route("sized-request-response-proto")
                .data(SizedMessageRequest.newBuilder().setSizeMb(sizeMb).build())
                .retrieveMono(ProtoSizedMessage.class)
                .block();
    }

    public static List<ProtoSizedMessage> sizedRequestStream(RSocketRequester rSocketRequester, int sizeMb) {
        return rSocketRequester.route("sized-request-stream-proto")
                .data(SizedMessageRequest.newBuilder().setSizeMb(sizeMb).build())
                .retrieveFlux(ProtoSizedMessage.class)
                .collectList()
                .block();
    }
}
