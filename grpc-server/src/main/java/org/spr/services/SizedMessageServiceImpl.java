package org.spr.services;

import com.google.protobuf.ByteString;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;
import org.spr.protos.ProtoSizedMessage;
import org.spr.protos.SizedMessageRequest;
import org.spr.protos.SizedMessageServiceGrpc;

/**
 * grpc service to return messages of requested size
 */
@GrpcService
public class SizedMessageServiceImpl extends SizedMessageServiceGrpc.SizedMessageServiceImplBase {

    /**
     * Responds wth an object of requested size
     *
     * @param request          Protobuf message with required size
     * @param responseObserver responseObserver to publish data to
     */
    @Override
    public void ping(SizedMessageRequest request, StreamObserver<ProtoSizedMessage> responseObserver) {
        int mb_size = request.getSizeMb();
        ProtoSizedMessage.Builder message = ProtoSizedMessage.newBuilder();
        for (int i = 0; i < mb_size; i++) {
            message.addData(ByteString.copyFrom(new byte[1024 * 1024]));
        }
        responseObserver.onNext(message.build());
        responseObserver.onCompleted();
    }

    /**
     * Responds wth a stream of objects (1MB each) of requested size
     *
     * @param request          Protobuf message with required size
     * @param responseObserver responseObserver to publish data to
     */
    @Override
    public void echoStream(SizedMessageRequest request, StreamObserver<ProtoSizedMessage> responseObserver) {
        int mb_size = request.getSizeMb();

        for (int i = 0; i < mb_size; i++) {
            responseObserver.onNext(ProtoSizedMessage
                    .newBuilder()
                    .addData(ByteString.copyFrom(new byte[1024 * 1024]))
                    .build()
            );
        }

        responseObserver.onCompleted();
    }
}
