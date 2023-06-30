package org.spr.service;

import org.spr.data.Message;
import org.spr.repository.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service for the Reactive mongo-repository
 */
@Service
public class MessageService {
    @Autowired
    private MessageRepository messageRepository;

    /**
     * gets message by id
     *
     * @param id id of the message
     * @return Message document with the requested id
     */
    public Mono<Message> getById(String id) {
        return messageRepository.findById(id);
    }

    /**
     * returns a stream of all messages in teh db
     *
     * @return Reactive stream of message documents
     */
    public Flux<Message> getAll() {
        return messageRepository.findAll();
    }

    /**
     * Creates a message in the db
     *
     * @param body body of the message
     * @return Mono containing the object id of the message created
     */
    public Mono<Message> create(String body) {
        return messageRepository.save(new Message(body));
    }

    /**
     * Removes a message in a db
     *
     * @param id Id of the message
     */
    public Mono<Void> remove(String id) {
        return messageRepository
                .deleteById(id);
    }
}
