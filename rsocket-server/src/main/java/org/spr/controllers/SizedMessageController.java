package org.spr.controllers;

import com.google.protobuf.ByteString;
import org.spr.data.SizedMessage;
import org.spr.protos.SimpleSizedMessage;
import org.spr.protos.SizedMessageRequest;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Controller
public class SizedMessageController {

    /**
     * Returns a data object of requested size
     *
     * @param sizeMb desired size in MB
     * @return A Mono containing a SizedMessage Data Object
     */
    @MessageMapping("sized-request-response")
    public Mono<SizedMessage> sizedRequestResponse(Integer sizeMb) {
        byte[][] data = new byte[sizeMb][];
        for (int i = 0; i < sizeMb; i++) {
            data[i] = new byte[1024 * 1024];
        }
        return Mono.just(new SizedMessage(data));
    }

    /**
     * Returns a stream of data objects (1MB each) of requested size
     *
     * @param sizeMb desired size in MB
     * @return A reactive stream containing SizedMessage Data Objects
     */
    @MessageMapping("sized-request-stream")
    public Flux<SizedMessage> sizedRequestStream(Integer sizeMb) {
        return Flux.range(0, sizeMb)
                .map((integer) -> {
                    byte[][] data = new byte[1][];
                    data[0] = new byte[1024 * 1024];
                    return new SizedMessage(data);
                });
    }

    /**
     * Returns a SizedMessage protobuf of requested size
     *
     * @param request a SizedMessageRequest protobuf with size in MB
     * @return A Mono containing a SizedMessage Data Object
     */
    @MessageMapping("sized-request-response-proto")
    public Mono<SimpleSizedMessage> sizedRequestResponseProto(SizedMessageRequest request) {
        int sizeMb = request.getSizeMb();
        SimpleSizedMessage.Builder message = SimpleSizedMessage.newBuilder();
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
    public Flux<SimpleSizedMessage> sizedRequestStreamProto(SizedMessageRequest request) {
        int sizeMb = request.getSizeMb();
        return Flux.range(0, sizeMb)
                .map((integer) -> SimpleSizedMessage
                        .newBuilder()
                        .addData(ByteString.copyFrom(new byte[1024 * 1024]))
                        .build()
                );
    }
}
