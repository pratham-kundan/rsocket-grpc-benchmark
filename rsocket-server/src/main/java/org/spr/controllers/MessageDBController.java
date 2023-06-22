package org.spr.controllers;

import org.spr.data.MessageDto;
import org.spr.protos.SimpleMessage;
import org.spr.service.MessageService;
import org.spr.utils.MessageUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
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
        return messageService.create(body);
    }

    /**
     * gets message by id
     *
     * @param id id of the message
     * @return Message Data Object with the requested id
     */
    @MessageMapping("get/{id}")
    public Mono<MessageDto> getById(@PathVariable String id) {
        return messageService
                .getById(id)
                .map(MessageUtils::messageToDto);
    }

    /**
     * returns a stream of all messages in the db
     *
     * @return Reactive stream of messages as data objects
     */
    @MessageMapping("get-all")
    public Flux<MessageDto> getAll() {
        return messageService.getAll().map(MessageUtils::messageToDto);
    }

    /**
     * gets message by id
     *
     * @param id id of the message
     * @return Message protobuf with the requested id
     */
    @MessageMapping("get-proto/{id}")
    public Mono<SimpleMessage> getProtoById(@PathVariable String id) {
        return messageService
                .getById(id)
                .map(MessageUtils::messageToProto);
    }

    /**
     * returns a stream of all messages in the db
     *
     * @return Reactive stream of messages as protobuf
     */
    @MessageMapping("get-all-proto")
    public Flux<SimpleMessage> getAllProto() {
        return messageService
                .getAll()
                .map(MessageUtils::messageToProto);
    }
}
