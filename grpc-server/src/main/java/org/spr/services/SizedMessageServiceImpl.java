package org.spr.services;

import com.google.protobuf.ByteString;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;
import org.spr.protos.SimpleSizedMessage;
import org.spr.protos.SizedMessageRequest;
import org.spr.protos.SizedMessageServiceGrpc;

@GrpcService
public class SizedMessageServiceImpl extends SizedMessageServiceGrpc.SizedMessageServiceImplBase {

    @Override
    public void ping(SizedMessageRequest request, StreamObserver<SimpleSizedMessage> responseObserver) {
        int mb_size = request.getSizeMb();
        SimpleSizedMessage.Builder message = SimpleSizedMessage.newBuilder();
        for (int i = 0; i < mb_size; i++) {
            message.addData(ByteString.copyFrom(new byte[1024 * 1024]));
        }
        responseObserver.onNext(message.build());
        responseObserver.onCompleted();
    }

    @Override
    public void echoStream(SizedMessageRequest request, StreamObserver<SimpleSizedMessage> responseObserver) {
        int mb_size = request.getSizeMb();

        for (int i = 0; i < mb_size; i++) {
            responseObserver.onNext(SimpleSizedMessage
                    .newBuilder()
                    .addData(ByteString.copyFrom(new byte[1024 * 1024]))
                    .build()
            );
        }

        responseObserver.onCompleted();
    }
}
