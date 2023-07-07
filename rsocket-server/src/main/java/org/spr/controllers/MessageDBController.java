package org.spr.controllers;

import org.spr.protos.ProtoMessage;
import org.spr.service.MessageService;
import org.spr.utils.MessageUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Controller
public class MessageDBController {

    @Autowired
    private MessageService messageService;

    /**
     * Creates a message in the db
     *
     * @param body body of the message
     * @return Mono containing the object id of the message created
     */
    @MessageMapping("create")
    public Mono<String> create(String body) {
        return messageService
                .create(body)
                .map(Object::toString);
    }

    /**
     * gets message by id
     *
     * @param request  request containing the id of the requested object.
     * @return Message protobuf with the requested id
     */
    @MessageMapping("get-proto")
    public Mono<ProtoMessage> getProtoById(ProtoMessage request) {
        return messageService
                .getById(request.getBody())
                .map(MessageUtils::messageToProto);
    }

    /**
     * returns a stream of all messages in the db
     *
     * @return Reactive stream of messages as protobuf
     */
    @MessageMapping("get-all-proto")
    public Flux<ProtoMessage> getAllProto() {
        return messageService
                .getAll()
                .map(MessageUtils::messageToProto);
    }

    /**
     * Bidirectional streaming channel
     * Stores messages from a reactive stream to a database
     *
     * @param request A reactive stream containing messages to add to the DB
     * @return A reactive stream containing an acknowledgement for each received message
     */
    @MessageMapping("push-all-proto")
    public Flux<ProtoMessage> pushAllProto(Flux<ProtoMessage> request) {
        return request
                .flatMap(message -> messageService.create(message.getBody()))
                .map(MessageUtils::messageToProto);
    }

    /**
     * Client streaming to server
     * Removes the messages from a stream from the database.
     *
     * @param request A reactive stream containing messages ids to remove from the DB
     * @return A mono  acknowledging for the number of received requests
     */
    @MessageMapping("remove-all-proto")
    public Mono<ProtoMessage> removeAllProto(Flux<ProtoMessage> request) {
        return request
                .doOnNext(item -> messageService.remove(item.getBody()).subscribe())
                .count()
                .map(deleted -> ProtoMessage
                        .newBuilder()
                        .setBody("Acknowledged: " + deleted + " requests")
                        .build()
                );
    }
}
