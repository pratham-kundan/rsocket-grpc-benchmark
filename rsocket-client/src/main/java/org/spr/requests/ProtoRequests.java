package org.spr.requests;

import org.spr.protos.SimpleMessage;
import org.spr.protos.SimpleSizedMessage;
import org.spr.protos.SizedMessageRequest;
import org.springframework.messaging.rsocket.RSocketRequester;

import java.util.List;

/**
 * Class to make requests to server and return protobuf objects
 */
public class ProtoRequests {
    public static SimpleMessage requestResponseProto(RSocketRequester rSocketRequester, String requestText) {
        return rSocketRequester.route("request-response-proto")
                .data(SimpleMessage.newBuilder().setBody(requestText).build())
                .retrieveMono(SimpleMessage.class)
                .block();
    }

    public static List<SimpleMessage> requestStreamProto(RSocketRequester rSocketRequester, String requestText) {
        return rSocketRequester.route("request-stream-proto")
                .data(SimpleMessage.newBuilder().setBody(requestText).build())
                .retrieveFlux(SimpleMessage.class)
                .collectList()
                .block();
    }

    public static List<SimpleMessage> getAllMessagesProto(RSocketRequester rSocketRequester) {
        return rSocketRequester
                .route("get-all-proto")
                .retrieveFlux(SimpleMessage.class)
                .collectList()
                .block();
    }

    public static SimpleSizedMessage sizedRequestResponseProto(RSocketRequester rSocketRequester, int sizeMb) {
        return rSocketRequester.route("sized-request-response-proto")
                .data(SizedMessageRequest.newBuilder().setSizeMb(sizeMb).build())
                .retrieveMono(SimpleSizedMessage.class)
                .block();
    }

    public static List<SimpleSizedMessage> sizedRequestStreamProto(RSocketRequester rSocketRequester, int sizeMb) {
        return rSocketRequester.route("sized-request-stream-proto")
                .data(SizedMessageRequest.newBuilder().setSizeMb(sizeMb).build())
                .retrieveFlux(SimpleSizedMessage.class)
                .collectList()
                .block();
    }
}
