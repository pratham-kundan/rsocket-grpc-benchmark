package org.spr.controllers;

import org.spr.data.MessageDto;
import org.spr.protos.ProtoMessage;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Instant;

@Controller
public class MessageController {
    /**
     * Returns the string that it got in the request
     *
     * @param request request text to be echoed back
     * @return Mono containing String with request-text with timestamp
     */
    @MessageMapping("request-response")
    public Mono<String> echo(String request) {
        return Mono.just("Acknowledged: " + request);
    }

    /**
     * Returns a reactive stream of strings from the request
     *
     * @param request request text to be echoed back
     * @return Flux flux containing request-text with timestamp
     */
    @MessageMapping("request-stream")
    public Flux<String> echoStream(String request) {
        return Flux.range(0, 200)
                .map(i -> "Responding to: " + request + " at:" + Instant.now());
    }

    /**
     * Returns a Message Data Object of strings from the request
     *
     * @param request request object text to be echoed back
     * @return Mono containing object with request-text with timestamp
     */
    @MessageMapping("request-response-dto")
    public Mono<MessageDto> echo(MessageDto request) {
        return Mono.just(new MessageDto("", "Acknowledged: " + request.getBody()));
    }

    /**
     * Returns a reactive stream of Message data object
     *
     * @param request request object with text to be echoed back
     * @return Flux containing object with request-text with timestamp
     */
    @MessageMapping("request-stream-dto")
    public Flux<MessageDto> echoStream(MessageDto request) {
        return Flux.range(0, 200)
                .map(i -> new MessageDto("", "Responding to: " + request + " at:" + Instant.now()));
    }

    /**
     * Returns a Message Protobuf with string from the request
     *
     * @param request request protobuf with text to be echoed back
     * @return Mono containing object with request-text with timestamp
     */
    @MessageMapping("request-response-proto")
    public Mono<ProtoMessage> echo(ProtoMessage request) {
        return Mono.just(ProtoMessage
                .newBuilder()
                .setBody("Acknowledged: " + request.getBody() + " at:" + Instant.now())
                .build()
        );
    }

    /**
     * Returns a reactive stream of Message data object
     *
     * @param request request protobuf with text to be echoed back
     * @return Flux containing protobuf with request-text with timestamp
     */
    @MessageMapping("request-stream-proto")
    public Flux<ProtoMessage> echoStream(ProtoMessage request) {
        return Flux.
                range(0, 200)
                .map(i -> ProtoMessage
                        .newBuilder()
                        .setBody("Responding to: " + request.getBody() + " at:" + Instant.now())
                        .build()
                );
    }
}
