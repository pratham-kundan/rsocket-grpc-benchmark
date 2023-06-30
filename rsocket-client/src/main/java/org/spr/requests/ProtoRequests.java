package org.spr.requests;

import org.spr.protos.ProtoMessage;
import org.spr.protos.ProtoSizedMessage;
import org.spr.protos.SizedMessageRequest;
import org.springframework.messaging.rsocket.RSocketRequester;
import reactor.core.publisher.Flux;

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

    public static ProtoMessage streamResponse(RSocketRequester rSocketRequester, String requestText) {
        Flux<ProtoMessage> request = Flux.range(1, 200)
                .map((__) -> ProtoMessage.newBuilder()
                        .setBody(requestText)
                        .build());
        return rSocketRequester
                .route("stream-response-proto")
                .data(request)
                .retrieveFlux(ProtoMessage.class)
                .blockLast();

    }

    public static List<ProtoMessage> biStream(RSocketRequester rSocketRequester, String requestText) {
        Flux<ProtoMessage> request = Flux.range(1, 200)
                .map((__) -> ProtoMessage.newBuilder()
                        .setBody(requestText)
                        .build());
        return rSocketRequester
                .route("bi-stream-proto")
                .data(request)
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

    public static List<ProtoMessage> pushAll(RSocketRequester rSocketRequester, List<String> messages) {
        return rSocketRequester
                .route("push-all-proto")
                .data(Flux.fromIterable(messages).map(message -> ProtoMessage.newBuilder().setBody(message).build()))
                .retrieveFlux(ProtoMessage.class)
                .collectList()
                .block();
    }

    public static ProtoMessage removeAll(RSocketRequester rSocketRequester, List<String> messageIds) {
        return rSocketRequester
                .route("remove-all-proto")
                .data(Flux
                        .fromIterable(messageIds)
                        .map(message -> ProtoMessage.newBuilder().setBody(message).build()))
                .retrieveFlux(ProtoMessage.class)
                .blockLast();
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
