package org.spr.services;

import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;
import org.spr.data.Message;
import org.spr.protos.MessageDbServiceGrpc;
import org.spr.protos.ProtoMessage;
import org.spr.repositories.ReactiveMessageRepository;
import org.spr.utils.MessageUtils;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Grpc Service to communicate with mongo
 */
@GrpcService
public class MessageDbServiceImpl extends MessageDbServiceGrpc.MessageDbServiceImplBase {
    @Autowired
    private ReactiveMessageRepository messageRepository;

    /**
     * Fetches a document in mongo db with supplied message id
     *
     * @param request          Protobuf message containing id
     * @param responseObserver responseObserver to publish data to
     */
    @Override
    public void getById(ProtoMessage request, StreamObserver<ProtoMessage> responseObserver) {
        ProtoMessage message =  messageRepository
                .findById(request.getBody())
                .map(MessageUtils::messageToProto)
                .block();
        responseObserver.onNext(message);
        responseObserver.onCompleted();
    }

    /**
     * Server streaming to client
     * Returns all messages in the mongo database
     *
     * @param request          empty request
     * @param responseObserver responseObserver to publish data to
     */
    @Override
    public void getAll(ProtoMessage request, StreamObserver<ProtoMessage> responseObserver) {
        messageRepository
                .findAll()
                .map(MessageUtils::messageToProto)
                .doOnNext(responseObserver::onNext)
                .doOnComplete(responseObserver::onCompleted)
                .subscribe();

    }

    /**
     * Bidirectional streaming (Channel)
     * Accepts a stream of protoMessages from the response observer and adds them to the database
     *
     * @param responseObserver responseObserver to publish data to
     * @return A stream of message ids of the pushed documents
     */
    @Override
    public StreamObserver<ProtoMessage> pushAll(StreamObserver<ProtoMessage> responseObserver) {
        return new StreamObserver<>() {
            @Override
            public void onNext(ProtoMessage value) {
                responseObserver.onNext(
                        messageRepository
                                .save(MessageUtils.protoToMessage(value))
                                .map(MessageUtils::messageToProto)
                                .block()
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

    /**
     * Client streaming to server
     * Accepts a stream of protoMessages containing object ids from the response observer
     * and removes them from the database
     *
     * @param responseObserver responseObserver to publish data to
     * @return A message containing the number of the delete requests received
     */
    @Override
    public StreamObserver<ProtoMessage> removeAll(StreamObserver<ProtoMessage> responseObserver) {
        return new StreamObserver<>() {
            int deleted = 0;

            @Override
            public void onNext(ProtoMessage value) {
                messageRepository.deleteById(value.getBody()).subscribe();
                deleted++;
            }

            @Override
            public void onError(Throwable t) {
                responseObserver.onError(t);
                responseObserver.onCompleted();
            }

            @Override
            public void onCompleted() {
                responseObserver.onNext(ProtoMessage.newBuilder().setBody("Acknowledged: " + deleted + " requests").build());
                responseObserver.onCompleted();
            }
        };
    }
}
