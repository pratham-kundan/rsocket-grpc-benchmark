package org.spr.controllers;

import org.spr.protos.ProtoMessage;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Instant;

@Controller
public class MessageController {
    /**
     * Simple Request Response
     * Returns a Message Protobuf with string from the request
     *
     * @param request request protobuf with text to be echoed back
     * @return Mono containing object with request-text with timestamp
     */
    @MessageMapping("request-response-proto")
    public Mono<ProtoMessage> requestResponseProto(ProtoMessage request) {
        return Mono.just(ProtoMessage
                .newBuilder()
                .setBody("Acknowledged: " + request.getBody() + " at:" + Instant.now())
                .build()
        );
    }

    /**
     * Server streaming to client
     * Returns a reactive stream of Message data object
     *
     * @param request request protobuf with text to be echoed back
     * @return Flux containing protobuf with request-text with timestamp
     */
    @MessageMapping("request-stream-proto")
    public Flux<ProtoMessage> requestStreamProto(ProtoMessage request) {
        return Flux
                .range(0, 200)
                .map(i -> ProtoMessage
                        .newBuilder()
                        .setBody("Responding to: " + request.getBody() + " at:" + Instant.now())
                        .build()
                );
    }

    /**
     * Client streaming to server
     * Returns a Mono containing the number of messages received at the end of the stream
     *
     * @param request A Reactive stream containing a message
     * @return Mono containing the number of messages received at the end of the stream
     */
    @MessageMapping("stream-response-proto")
    public Mono<ProtoMessage> streamResponseProto(final Flux<ProtoMessage> request) {
        return request
                .count()
                .map(l -> ProtoMessage
                        .newBuilder()
                        .setBody("Received: " + l + " requests")
                        .build()
                );
    }

    /**
     * Bidirectional streaming (Channel)
     * Returns a Reactive stream containing an acknowledgement for each message received
     *
     * @param request A Reactive stream containing a message
     * @return Mono containing the number of messages received at the end of the stream
     */
    @MessageMapping("bi-stream-proto")
    public Flux<ProtoMessage> biStreamProto(final Flux<ProtoMessage> request) {
        return request
                .map(message -> message
                        .toBuilder()
                        .setBody("Responding to: " + message.getBody() + " at:" + Instant.now())
                        .build()
                );
    }
}
