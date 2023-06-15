package org.spr.services;

import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;
import org.spr.data.Message;
import org.spr.protos.MessageDbServiceGrpc;
import org.spr.protos.SimpleMessage;
import org.spr.repositories.MessageRepository;
import org.spr.utils.MessageUtils;
import org.springframework.beans.factory.annotation.Autowired;

@GrpcService
public class MessageDbServiceImpl extends MessageDbServiceGrpc.MessageDbServiceImplBase {
    @Autowired
    private MessageRepository messageRepository;

    @Override
    public void create(SimpleMessage request, StreamObserver<SimpleMessage> responseObserver) {
        Message saved = messageRepository.save(MessageUtils.protoToMessage(request));
        responseObserver.onNext(SimpleMessage
                .newBuilder()
                .setId(saved.getId())
                .build());
        responseObserver.onCompleted();
    }

    @Override
    public void getAll(SimpleMessage request, StreamObserver<SimpleMessage> responseObserver) {
        messageRepository
                .findAll()
                .stream()
                .map(MessageUtils::messageToProto)
                .forEach(responseObserver::onNext);
        responseObserver.onCompleted();
    }
}
