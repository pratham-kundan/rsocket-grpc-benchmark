package org.spr.services;

import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;
import org.spr.data.Message;
import org.spr.protos.MessageDbServiceGrpc;
import org.spr.protos.SimpleMessage;
import org.spr.repositories.MessageRepository;
import org.spr.utils.MessageUtils;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Grpc Service to communicate with mongo
 */
@GrpcService
public class MessageDbServiceImpl extends MessageDbServiceGrpc.MessageDbServiceImplBase {
    @Autowired
    private MessageRepository messageRepository;

    /**
     * Creates a document in mongo db with supplied body-text
     *
     * @param request          Protobuf message containing id
     * @param responseObserver responseObserver to publish data to
     */
    @Override
    public void create(SimpleMessage request, StreamObserver<SimpleMessage> responseObserver) {
        Message saved = messageRepository.save(MessageUtils.protoToMessage(request));
        responseObserver.onNext(SimpleMessage
                .newBuilder()
                .setId(saved.getId())
                .build());
        responseObserver.onCompleted();
    }

    /**
     * Returns all messages in the mongo database
     *
     * @param request          empty request
     * @param responseObserver responseObserver to publish data to
     */
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
