package org.spr.controllers;

import com.google.protobuf.ByteString;
import org.spr.protos.ProtoSizedMessage;
import org.spr.protos.SizedMessageRequest;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Controller
public class SizedMessageController {
    /**
     * Returns a SizedMessage protobuf of requested size
     *
     * @param request a SizedMessageRequest protobuf with size in MB
     * @return A Mono containing a SizedMessage Data Object
     */
    @MessageMapping("sized-request-response-proto")
    public Mono<ProtoSizedMessage> sizedRequestResponseProto(SizedMessageRequest request) {
        int sizeMb = request.getSizeMb();
        ProtoSizedMessage.Builder message = ProtoSizedMessage.newBuilder();
        for (int i = 0; i < sizeMb; i++) {
            message.addData(ByteString.copyFrom(new byte[1024 * 1024]));
        }
        return Mono.just(message.build());
    }

    /**
     * Returns a reactive stream of SizedMessage protobuf of requested size
     *
     * @param request a SizedMessageRequest protobuf with size in MB
     * @return A Flux containing SizedMessage protobuf (of 1MB each)
     */
    @MessageMapping("sized-request-stream-proto")
    public Flux<ProtoSizedMessage> sizedRequestStreamProto(SizedMessageRequest request) {
        int sizeMb = request.getSizeMb();
        return Flux.range(0, sizeMb)
                .map((integer) -> ProtoSizedMessage
                        .newBuilder()
                        .addData(ByteString.copyFrom(new byte[1024 * 1024]))
                        .build()
                );
    }
}
