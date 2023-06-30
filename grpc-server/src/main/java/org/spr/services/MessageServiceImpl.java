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
     * Request response
     * Echos the request body with timestamp
     *
     * @param request          Protobuf message with some text
     * @param responseObserver responseObserver to publish data to
     */
    @Override
    public void requestResponse(ProtoMessage request, StreamObserver<ProtoMessage> responseObserver) {
        ProtoMessage response = ProtoMessage
                .newBuilder()
                .setBody("Acknowledged: " + request.getBody() + " at: " + Instant.now())
                .build();
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    /**
     * Server streaming to client
     * Returns a stream of objects containing the request body with timestamp
     *
     * @param request          Protobuf message with some text
     * @param responseObserver responseObserver to stream data to
     */
    @Override
    public void requestStream(ProtoMessage request, StreamObserver<ProtoMessage> responseObserver) {
        for (int i = 0; i < 200; i++) {
            ProtoMessage response = ProtoMessage.newBuilder()
                    .setBody("Acknowledged: " + request.getBody() + " at: " + Instant.now())
                    .build();

            responseObserver.onNext(response);
        }

        responseObserver.onCompleted();
    }

    /**
     * Client streaming to server
     * Returns a single response from the server for a stream of elements
     *
     * @param responseObserver responseObserver to stream data to
     * @return A stream that returns a message containing the number of requests received
     * at the end of the stream
     */
    @Override
    public StreamObserver<ProtoMessage> streamResponse(StreamObserver<ProtoMessage> responseObserver) {
        final int[] received = {0};
        return new StreamObserver<>() {
            @Override
            public void onNext(ProtoMessage value) {
                received[0]++;
            }

            @Override
            public void onError(Throwable t) {
                responseObserver.onError(t);
                responseObserver.onCompleted();
            }

            @Override
            public void onCompleted() {
                responseObserver.onNext(ProtoMessage
                        .newBuilder()
                        .setBody("Received: " + received[0] + " requests")
                        .build());
                responseObserver.onCompleted();
            }
        };
    }

    /**
     * A bidirectional stream channel
     * Returns a single response from the server for a stream of elements
     *
     * @param responseObserver responseObserver to stream data to
     * @return A stream that containing the acknowledgement of the requests received
     */
    @Override
    public StreamObserver<ProtoMessage> biStream(StreamObserver<ProtoMessage> responseObserver) {
        return new StreamObserver<>() {
            @Override
            public void onNext(ProtoMessage value) {
                responseObserver.onNext(
                    value.toBuilder()
                        .setBody("Responding to: " + value.getBody() + " at:" + Instant.now())
                        .build()
                );
            }

            @Override
            public void onError(Throwable t) {
                responseObserver.onError(t);
                responseObserver.onCompleted();
            }

            @Override
            public void onCompleted() {
                responseObserver.onCompleted();
            }
        };
    }
}
