package org.spr.services;

import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;
import org.spr.protos.MessageServiceGrpc;
import org.spr.protos.ProtoMessage;

import java.time.Instant;

/**
 * Grpc service to echo back messages
 */
@GrpcService
public class MessageServiceImpl extends MessageServiceGrpc.MessageServiceImplBase {
    /**
     * Echos the request body with timestamp
     *
     * @param request          Protobuf message with some text
     * @param responseObserver responseObserver to publish data to
     */
    @Override
    public void ping(ProtoMessage request, StreamObserver<ProtoMessage> responseObserver) {
        ProtoMessage response = ProtoMessage
                .newBuilder()
                .setBody("Acknowledged: " + request.getBody() + " at: " + Instant.now())
                .build();
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    /**
     * Returns a stream of objects containing the request body with timestamp
     *
     * @param request          Protobuf message with some text
     * @param responseObserver responseObserver to stream data to
     */
    @Override
    public void echoStream(ProtoMessage request, StreamObserver<ProtoMessage> responseObserver) {
        for (int i = 0; i < 200; i++) {
            ProtoMessage response = ProtoMessage.newBuilder()
                    .setBody("Acknowledged: " + request.getBody() + " at: " + Instant.now())
                    .build();

            responseObserver.onNext(response);
        }

        responseObserver.onCompleted();
    }
}
