package org.spr.repositories;

import org.spr.data.Message;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface ReactiveMessageRepository extends ReactiveMongoRepository<Message, String> {
}
